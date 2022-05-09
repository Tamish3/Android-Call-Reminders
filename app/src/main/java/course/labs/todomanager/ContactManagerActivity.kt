package course.labs.todomanager

import android.Manifest
import android.Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.text.ParseException

import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Duration
import java.time.Period
import java.time.ZonedDateTime



/** This class resembles a template similar to that of the Lab5 UILabs that we covered
 * but all the code was added seperately for the purposes of this project
 * Source: https://gitlab.cs.umd.edu/arasevic/cmsc436spring2022-student/-/tree/main/Labs/Lab5_UILabs
 * */
class ContactManagerActivity : AppCompatActivity() {

    private var enableNotificationListenerAlertDialog: AlertDialog? = null

    // OnCreate
    public override fun onCreate(savedInstanceState: Bundle?) {
        //get content view and set actionbar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycle_view)
        var toolbar= findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar)

        Log.i(TAG, "Entered onCreate()")

        mAdapter = ContactListAdapter(this)

        var x = findViewById<RecyclerView>(R.id.list)
        x.layoutManager = LinearLayoutManager(this)

        // Load saved ContactItems
        loadItemsFromFile()

        //taking care of permissions
        if (checkPermission()) {
            x.adapter = mAdapter
        }
        else {
            getPermission()
            x.adapter = mAdapter
        }

        // action listener for the add button that takes us to the Add to do Activity
        findViewById<FloatingActionButton>(R.id.addbutton)?.setOnClickListener {
            Log.i(ContactManagerActivity.TAG, "Entered addbuttpn.OnClickListener.onClick()")

            val options: Bundle? = null
            ActivityCompat.startActivityForResult(
                this,
                Intent(
                    this,
                    AddContactActivity::class.java
                ),
                ADD_CONTACT_ITEM_REQUEST,
                options
            )
        }

        //Notification Service Permission
        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog?.show();
        }

        val notificationHelper : NotificationHelper? = null
        val filter : IntentFilter = IntentFilter("android.service.notification.NotificationListenerService")
        registerReceiver(notificationHelper, filter)


    }

    //Notification Service Permission Checker
    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName: String = "course.labs.todomanager"
        val flat: String = Settings.Secure.getString(
            applicationContext.getContentResolver(),
            "enabled_notification_listeners"
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    //Get Notifications Permissions
    /** Source: https://github.com/Chagall/notification-listener-service-example/blob/master/app/src/main/java/com/github/chagall/notificationlistenerexample/MainActivity.java */
    private fun buildNotificationServiceAlertDialog(): AlertDialog? {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.read_notifications_string)
        alertDialogBuilder.setMessage(R.string.read_notifications_explanation)
        alertDialogBuilder.setPositiveButton(R.string.yes,
            DialogInterface.OnClickListener { dialog, id ->
                startActivity(
                    Intent(
                        ACTION_NOTIFICATION_LISTENER_SETTINGS
                    )
                )
            })
        alertDialogBuilder.setNegativeButton(R.string.no,
            DialogInterface.OnClickListener { dialog, id ->
                // If you choose to not enable the notification listener
                // the app. will not work as expected
            })
        return alertDialogBuilder.create()
    }

    //Broadcast reciever for notifcations
    class NotificationHelper : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i(TAG, "Notification helper")
        }
    }

    //Checking for Read Contacts Permission
    fun checkPermission(): Boolean {
        return PermissionChecker.checkSelfPermission(
            this, READ_CONTACTS_PERM
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    //Getting all the permissions at the start
    fun getPermission() {
        ActivityCompat.requestPermissions(this as Activity, arrayOf(READ_CONTACTS_PERM, READ_PHONE_STATE_PERM, READ_CALL_LOG_PERM, PROCESS_OUTGOING_CALS_PERM, SCHEDULE_EXACT_ALARM, BIND_NOTIFICATION_LISTENER_SERVICE, ACCESS_NOTIFICATION_POLICY),
            PERMISSIONS_PICK_CONTACT_REQUEST
        )
    }

    //Get OnActivityResult from  AddContactActivity and also UpdateContacActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(TAG, "Entered onActivityResult()")

        // if user submitted a new ContactItem
        // Create a new ContactItem from the data Intent
        // and then add it to the adapter
        if (resultCode== RESULT_OK && requestCode == ADD_CONTACT_ITEM_REQUEST) {
            Log.i(TAG, "Getting Entered")
            Log.i(TAG, "a ${mAdapter!!.itemCount} count")
            mAdapter!!.add(ContactItem(data!!))
        } else if (resultCode== RESULT_OK && requestCode == UPDATE_CONTACT_ITEM_REQUEST){
            Log.i(TAG, "Getting Entered into Update")
            mAdapter!!.update(ContactItem(data!!))
        } else if (resultCode== RESULT_FIRST_USER && requestCode == UPDATE_CONTACT_ITEM_REQUEST) {
            mAdapter!!.delete(ContactItem(data!!))
        }

    }


    public override fun onResume() {
        super.onResume()
    }

    //saving info to files
    override fun onPause() {
        super.onPause()
        // Save ContactItems to file
        saveItemsToFile()
    }

    //Creating the options bar with delete all option
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all")
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_DELETE -> {
                mAdapter!!.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //load all the items from file onCreate()
    private fun loadItemsFromFile() {
        var reader: BufferedReader? = null
        try {
            val fis = openFileInput(FILE_NAME)
            reader = BufferedReader(InputStreamReader(fis))

            var icon: String?
            var name: String?
            var deadline: ZonedDateTime?
            var phoneNumber: String?
            var oldTime: ZonedDateTime?
            var dateRange: Period?
            var timeRange: Duration?
            var counter = 0
            do {
                icon = reader.readLine()
                name = reader.readLine()
                if (name == null) {
                    break
                }
                deadline = ZonedDateTime.parse(reader.readLine())
                phoneNumber = reader.readLine()
                oldTime = ZonedDateTime.parse(reader.readLine())
                dateRange = Period.parse(reader.readLine())
                timeRange = Duration.parse(reader.readLine())
                reader.readLine()

                mAdapter!!.add(ContactItem(icon, name, deadline, phoneNumber, oldTime, dateRange, timeRange))
            }
            while (true)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        } finally {
            if (null != reader) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    // Save ContactItems to file
    private fun saveItemsToFile() {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(BufferedWriter(OutputStreamWriter(
                fos)))

            for (idx in 1 until mAdapter!!.itemCount) {
                writer.println(mAdapter!!.getItem(idx))
                Log.i(TAG, mAdapter!!.getItem(idx).toString())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }

    //all objects
    companion object {

        const val ADD_CONTACT_ITEM_REQUEST = 0
        const val UPDATE_CONTACT_ITEM_REQUEST = 1
        private const val FILE_NAME = "ContactManagerActivityData.txt"
        const val TAG = "CallYourMother"

        // IDs for menu items - just the delete
        private const val MENU_DELETE = Menu.FIRST
        var mAdapter : ContactListAdapter? = null
        private const val PERMISSIONS_PICK_CONTACT_REQUEST = 1
        private const val READ_CONTACTS_PERM = Manifest.permission.READ_CONTACTS
        private const val READ_PHONE_STATE_PERM = Manifest.permission.READ_PHONE_STATE
        private const val READ_CALL_LOG_PERM = Manifest.permission.READ_CALL_LOG
        private const val PROCESS_OUTGOING_CALS_PERM = Manifest.permission.PROCESS_OUTGOING_CALLS
        private const val ACCESS_NOTIFICATION_POLICY = Manifest.permission.ACCESS_NOTIFICATION_POLICY
    }
}

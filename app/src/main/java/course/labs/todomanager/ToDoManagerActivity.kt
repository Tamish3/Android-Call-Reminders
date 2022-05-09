package course.labs.todomanager

import android.Manifest
import android.Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.annotation.SuppressLint
import android.app.ActionBar
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
import java.util.Date

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Duration
import java.time.Period
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class ToDoManagerActivity : AppCompatActivity() {

    private var enableNotificationListenerAlertDialog: AlertDialog? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycle_view)
        var toolbar= findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar)
        Log.i(TAG, "Entered onCreate()")

        // Todo - Create a new TodoListAdapter for this Activity's RecyclerView
        mAdapter = ToDoListAdapter(this)

        var x = findViewById<RecyclerView>(R.id.list)
        x.layoutManager = LinearLayoutManager(this)

        // Load saved ToDoItems
        loadItemsFromFile()

        // TODO - Attach the adapter to this Activity's RecyclerView
        if (checkPermission()) {
            x.adapter = mAdapter
        }
        else {
            getPermission()
            x.adapter = mAdapter
        }

       findViewById<FloatingActionButton>(R.id.addbutton)?.setOnClickListener {
            Log.i(ToDoManagerActivity.TAG, "Entered footerView.OnClickListener.onClick()")

            val options: Bundle? = null
            ActivityCompat.startActivityForResult(
                this,
                Intent(
                    this,
                    AddToDoActivity::class.java
                ),
                ADD_TODO_ITEM_REQUEST,
                options
            )
        }

        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog?.show();
        }

        val notificationHelper : NotificationHelper? = null
        val filter : IntentFilter = IntentFilter("android.service.notification.NotificationListenerService")
        registerReceiver(notificationHelper, filter)


    }

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

    //https://github.com/Chagall/notification-listener-service-example/blob/master/app/src/main/java/com/github/chagall/notificationlistenerexample/MainActivity.java
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

    class NotificationHelper : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i(TAG, "Notification helper")
        }
    }

    fun checkPermission(): Boolean {
        return PermissionChecker.checkSelfPermission(
            this, READ_CONTACTS_PERM
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    fun getPermission() {
        ActivityCompat.requestPermissions(this as Activity, arrayOf(READ_CONTACTS_PERM, READ_PHONE_STATE_PERM, READ_CALL_LOG_PERM, PROCESS_OUTGOING_CALS_PERM, SCHEDULE_EXACT_ALARM, BIND_NOTIFICATION_LISTENER_SERVICE, ACCESS_NOTIFICATION_POLICY),
            PERMISSIONS_PICK_CONTACT_REQUEST
        )


//        requestPermissions(mContext as Activity, arrayOf(READ_CONTACTS_PERM), PERMISSIONS_PICK_CONTACT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(TAG, "Entered onActivityResult()")

        // TODO - Check result code and request code
        // if user submitted a new ToDoItem
        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter
        if (resultCode== RESULT_OK && requestCode == ADD_TODO_ITEM_REQUEST) {
            Log.i(TAG, "Getting Entered")
            Log.i(TAG, "a ${mAdapter!!.itemCount} count")
            mAdapter!!.add(ToDoItem(data!!))
        } else if (resultCode== RESULT_OK && requestCode == UPDATE_TODO_ITEM_REQUEST){
            Log.i(TAG, "Getting Entered into Update")
            mAdapter!!.update(ToDoItem(data!!))
        } else if (resultCode== RESULT_FIRST_USER && requestCode == UPDATE_TODO_ITEM_REQUEST) {
            mAdapter!!.delete(ToDoItem(data!!))
        }

    }

    // Do not modify below here

    public override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        // Save ToDoItems to file
        saveItemsToFile()
    }

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

                mAdapter!!.add(ToDoItem(icon, name, deadline, phoneNumber, oldTime, dateRange, timeRange))
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

    // Save ToDoItems to file
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

    companion object {

        const val ADD_TODO_ITEM_REQUEST = 0
        const val UPDATE_TODO_ITEM_REQUEST = 1
        const val DELETE_TODO_ITEM_REQUEST = 2
        private const val FILE_NAME = "TodoManagerActivityData.txt"
        const val TAG = "Lab-UserInterface"

        // IDs for menu items
        private const val MENU_DELETE = Menu.FIRST
        var mAdapter : ToDoListAdapter? = null
        private var hasPermission: Boolean = false
        private const val PERMISSIONS_PICK_CONTACT_REQUEST = 1
        private const val READ_CONTACTS_PERM = Manifest.permission.READ_CONTACTS
        private const val READ_PHONE_STATE_PERM = Manifest.permission.READ_PHONE_STATE
        private const val READ_CALL_LOG_PERM = Manifest.permission.READ_CALL_LOG
        private const val PROCESS_OUTGOING_CALS_PERM = Manifest.permission.PROCESS_OUTGOING_CALLS
        private const val ACCESS_NOTIFICATION_POLICY = Manifest.permission.ACCESS_NOTIFICATION_POLICY
    }
}

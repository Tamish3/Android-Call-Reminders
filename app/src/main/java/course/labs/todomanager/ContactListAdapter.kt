package course.labs.todomanager



import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


/**
 * The class in which the List of the contacts are being stored
 * and also how they are manipulateed during an update or delete
 * by a user or also when they need to get update because of
 * the CallListener aor Notification Listener
 */
class ContactListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    private val mItems = ArrayList<ContactItem>()
    private val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    //update when a call or notification is recieved so the deadline is
    //just being incremented
    fun updateTo(name: String) {
        var counter = -1
        for (contact in mItems) {
            counter++
            if(contact.name == name) {
                contact.oldTime = contact.deadline
                contact.deadline = contact.deadline?.plus(contact.dateRange)?.plus(contact.timeRange)
                break
            }
        }

        //list and alarm manager are updated accordingly
        val item = mItems[counter]
        notifyDataSetChanged()


        val notifyIntent = Intent(mContext, NotificationReceiver::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            item.name.hashCode(),
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = item.deadline?.toEpochSecond()?.times(1000)!!
        }
        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    //adding a new contact to the list
    fun add(item: ContactItem) {
        var helper = true;

        //make sure not adding a duplicate
        for(contact in mItems) {
            if(contact.name == item.name) {
                helper = false;
            }
        }

        //if not adding a duplicate goes through if else it enters
        //the else and sends a AlertDialog
        if(helper) {
            mItems.add(item)
            notifyItemChanged(mItems.size)
         } else { //Source : //https://www.tutorialkart.com/kotlin-android/android-alert-dialog-example/
            val dialogBuilder = AlertDialog.Builder(mContext)
            // setting the message
            dialogBuilder.setMessage("You can update by clicking the settings icon to the corresponding contact.")
                .setCancelable(false)
                .setNegativeButton("OK", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // creating a dialog box
            val alert = dialogBuilder.create()
            alert.setTitle("${item.name} was already added")
            alert.show()
        }
    }

    //Takes care of update from the user from the UpdateContactActivity
    fun update(item: ContactItem) {
        //finds which item to update and updates it
        for (contact in mItems) {
            if (contact.name == item.name) {
                if (contact.deadline!!.isAfter(ZonedDateTime.now())) {
                    contact.oldTime = ZonedDateTime.now()
                    contact.deadline = ZonedDateTime.now().plus(item.dateRange).plus(item.timeRange)
                } else {
                    contact.deadline = item.deadline
                }
                contact.dateRange = item.dateRange
                contact.timeRange = item.timeRange
            }
        }


        //notifies that the data set has been changed and also
        //updates the alarm manager accordingly
        notifyDataSetChanged()

        val notifyIntent = Intent(mContext, NotificationReceiver::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            item.name.hashCode(),
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = item.deadline?.toEpochSecond()?.times(1000)!!
        }

        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }

    //When user wants to delete all the contacts
    //that are being tracked of deleteAll() is called
    fun deleteAll() {
        val iterator = mItems.iterator()

        //goes through all the items in the list
        while(iterator.hasNext()) {
            val item = iterator.next()
            iterator.remove()

            //delete alarmManager for this contact
            val notifyIntent = Intent(mContext, NotificationReceiver::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getBroadcast(
                mContext,
                item.name.hashCode(),
                notifyIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            notifyDataSetChanged()
        }
    }

    //when user wants delete a single Contact
    // inf the UpdateContactActivity
    fun delete(item: ContactItem) {
        for (contact in mItems) {
            if(contact.name == item.name) {
                mItems.remove(contact)
                break
            }
        }

        notifyDataSetChanged()

        //delete alarmManager for this contact
        val notifyIntent = Intent(mContext, NotificationReceiver::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            item.name.hashCode(),
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun getItem(pos: Int): Any {
        return mItems[pos - 1]
    }

    // Returns the number of ContactItems
    override fun getItemCount(): Int {
        return mItems.size + 1
    }

    // returns if the
    override fun getItemViewType(position: Int): Int {
        return if (position==0) HEADER_VIEW_TYPE else CONTACT_VIEW_TYPE
    }

    //cretaing a notification channel
    private fun createNotificationChannel() {
        mChannelID = "course.labs.todomanager.channel_01"

        // The user-visible name of the channel.
        val name = "NotificationChannel"
        // The user-visible description of the channel
        val description = "Notification channel for this app"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelID, name, importance)

        // Configure notification channel
        mChannel.description = description
        mChannel.enableLights(true)

        // Sets the notification light color for notifications posted
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = mVibratePattern

        mNotificationManager.createNotificationChannel(mChannel)
    }

    // Retrieve the number of ContactItems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")

        //notification channel
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        if (viewType == HEADER_VIEW_TYPE) {
            Log.i(TAG, "NOT adding contact onCreateViewHolder()   ")
            val v = LayoutInflater.from(parent.context).inflate(R.layout.header_view, parent, false)
            v
            return ViewHolder(v)
        } else {
            Log.i(TAG, "adding contact onCreateViewHolder()   ")

            val v = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
            val viewHolder = ViewHolder(v)

            viewHolder.mIconView = v.findViewById(R.id.iconView)
            viewHolder.mNameView=v.findViewById(R.id.nameView)
            viewHolder.mTimeLeftView = v.findViewById((R.id.timeLeftView))
            viewHolder.mUpdateView = v.findViewById(R.id.settingsButtonItem)
            viewHolder.mAddView = v.findViewById(R.id.addbutton)

            viewHolder.mItemLayout=v


            return viewHolder
        }
    }

    //adds the contacts to the view
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        val button = mContext.findViewById()
        if (position == 0) {
            Log.i(ContactManagerActivity.TAG, "Entered footerView.OnClickListener.onClick()")
        } else {
            val ContactItem = mItems[position - 1]

            Log.i(TAG, "onBindViewHolder   " + viewHolder.mNameView.toString())
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - hh:mm a z")

            if (ContactItem.icon != "") {
                viewHolder.mIconView!!.setImageURI(Uri.parse(ContactItem.icon))
            } else {
                viewHolder.mIconView?.setImageDrawable(getDrawable(mContext, R.drawable.ic_account_circle))
            }
            viewHolder.mNameView?.text = ContactItem.name
            viewHolder.mTimeLeftView?.text = ContactItem.deadline!!.format(formatter)

            //notify
            ContactItem.name?.let { createNotification(it, ContactItem.deadline!!) }

            viewHolder.mUpdateView?.setOnClickListener {
                Log.i(ContactManagerActivity.TAG, "Entered footerView.OnClickListener.onClick()")

                val abc = Intent(mContext, UpdateContactActivity::class.java)
                abc.putExtra("icon", mItems[position-1].icon)
                abc.putExtra("name", mItems[position-1].name)
                abc.putExtra("phoneNumber", mItems[position-1].phoneNumber)
                abc.putExtra("oldTime", mItems[position-1].oldTime)
                abc.putExtra("dateRange", mItems[position-1].dateRange)
                abc.putExtra("timeRange", mItems[position-1].timeRange)

                startActivityForResult(mContext as Activity, abc, ContactManagerActivity.UPDATE_CONTACT_ITEM_REQUEST, null)
            }

            viewHolder.mAddView?.setOnClickListener {
                Log.i(ContactManagerActivity.TAG, "Entered footerView.OnClickListener.onClick()")

                val options: Bundle? = null
                startActivityForResult(
                    mContext as Activity,
                    Intent(
                        mContext,
                        AddContactActivity::class.java
                    ),
                    ContactManagerActivity.ADD_CONTACT_ITEM_REQUEST,
                    options
                )
            }
        }
    }

    //create the notification for the first time
    @RequiresApi(Build.VERSION_CODES.S)
    private fun createNotification(name: String, deadline: ZonedDateTime) {
        val notifyIntent = Intent(mContext, NotificationReceiver::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        notifyIntent.putExtra("name", name)


        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            name.hashCode(),
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = deadline.toEpochSecond() * 1000
        }

        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }

    // Get the ID for the ContactItem
    override fun getItemId(pos: Int): Long {
        return pos.toLong() - 1
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemLayout: View = itemView
        var mIconView: ImageView? = null;

        var mNameView: TextView? = null
        var mTimeLeftView: TextView? = null
        var mUpdateView: ImageView? = null
        var mAddView: FloatingActionButton? = null
    }

    companion object {

        private const val TAG = "ContactListAdapter"
        private const val HEADER_VIEW_TYPE = R.layout.header_view
        private const val CONTACT_VIEW_TYPE = R.layout.contact_item

        private lateinit var mNotificationManager: NotificationManager
        private lateinit var mChannelID: String

        private val mVibratePattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
    }


}

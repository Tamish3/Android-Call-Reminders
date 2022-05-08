package course.labs.todomanager



import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ToDoListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    private val mItems = ArrayList<ToDoItem>()
    private val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    fun updateTo(name: String) {
        var counter = -1
        for (contact in mItems) {
            counter++
            if(contact.name == name) {
//                if (ZonedDateTime.now().compareTo(contact.deadline) > 0) {
//
//                }
                contact.oldTime = contact.deadline
                contact.deadline = contact.deadline?.plus(contact.dateRange)?.plus(contact.timeRange)
                break
            }
        }
        val item = mItems[counter]
        notifyDataSetChanged()

        val notifyIntent = Intent(mContext, NotificationReceiver::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
//            0,
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

    fun add(item: ToDoItem) {
        var helper = true;
        for(contact in mItems) {
            if(contact.name == item.name) {
                helper = false;
            }
        }
        if(helper) {
            mItems.add(item)
            notifyItemChanged(mItems.size)
            //https://www.tutorialkart.com/kotlin-android/android-alert-dialog-example/

        } else {
            val dialogBuilder = AlertDialog.Builder(mContext)

            // set message of alert dialog
            dialogBuilder.setMessage("You can update by clicking the settings icon to the corresponding contact.")
                // if the dialog is cancelable
                .setCancelable(false)
                // negative button text and action
                .setNegativeButton("OK", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("${item.name} was already added")
            // show alert dialog
            alert.show()
        }
    }

    fun update(item: ToDoItem) {
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
        notifyDataSetChanged()

        val notifyIntent = Intent(mContext, NotificationReceiver::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
//            0,
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

    fun deleteAll() {
        for (contact in mItems) {
            delete(contact)
        }
    }

    fun delete(item: ToDoItem) {
        for (contact in mItems) {
            if(contact.name == item.name) {
                mItems.remove(contact)
            }
        }
        notifyDataSetChanged()

        //delete alarmManager for this contact
        val notifyIntent = Intent(mContext, NotificationReceiver::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
//            0,
            item.name.hashCode(),
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    // Clears the list adapter of all items.
    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }

    fun getItem(pos: Int): Any {
        return mItems[pos - 1]
    }

    // Returns the number of ToDoItems

    override fun getItemCount(): Int {
        return mItems.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==0) HEADER_VIEW_TYPE else TODO_VIEW_TYPE
    }

    private fun createNotificationChannel() {
        mChannelID = "course.labs.todomanager.channel_01"

        // The user-visible name of the channel.
        val name = "NotificationChannel"
        // The user-visible description of the channel
        val description = "Notification channel for this app"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelID, name, importance)

        // Configure the notification channel.
        mChannel.description = description
        mChannel.enableLights(true)

        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = mVibratePattern

        mNotificationManager.createNotificationChannel(mChannel)
    }

    // Retrieve the number of ToDoItems
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

            val v = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
            val viewHolder = ViewHolder(v)

            // TODO - Inflate the View (defined in todo_item.xml) for this ToDoItem and store references in ViewHolder
            viewHolder.mIconView = v.findViewById(R.id.iconView)
            viewHolder.mNameView=v.findViewById(R.id.nameView)
            viewHolder.mTimeLeftView = v.findViewById((R.id.timeLeftView))
            viewHolder.mUpdateView = v.findViewById(R.id.settingsButtonItem)
            viewHolder.mAddView = v.findViewById(R.id.addbutton)

            viewHolder.mItemLayout=v


            return viewHolder
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        val button = mContext.findViewById()
        if (position == 0) {
            Log.i(ToDoManagerActivity.TAG, "Entered footerView.OnClickListener.onClick()")
        } else {
            val toDoItem = mItems[position - 1]

            Log.i(TAG, "onBindViewHolder   " + viewHolder.mNameView.toString())
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - hh:mm a z")

            if (toDoItem.icon != "") {
                viewHolder.mIconView!!.setImageURI(Uri.parse(toDoItem.icon))
            } else {
                viewHolder.mIconView?.setImageDrawable(getDrawable(mContext, R.drawable.ic_account_circle))
            }
            viewHolder.mNameView?.text = toDoItem.name
            viewHolder.mTimeLeftView?.text = toDoItem.deadline!!.format(formatter)

            //notify
            toDoItem.name?.let { createNotification(it, toDoItem.deadline!!) }

            viewHolder.mUpdateView?.setOnClickListener {
                Log.i(ToDoManagerActivity.TAG, "Entered footerView.OnClickListener.onClick()")

                val abc = Intent(mContext, UpdateToDoActivity::class.java)
                abc.putExtra("icon", mItems[position-1].icon)
                abc.putExtra("name", mItems[position-1].name)
                abc.putExtra("phoneNumber", mItems[position-1].phoneNumber)
                abc.putExtra("oldTime", mItems[position-1].oldTime)
                abc.putExtra("dateRange", mItems[position-1].dateRange)
                abc.putExtra("timeRange", mItems[position-1].timeRange)

                startActivityForResult(mContext as Activity, abc, ToDoManagerActivity.UPDATE_TODO_ITEM_REQUEST, null)
            }

            viewHolder.mAddView?.setOnClickListener {
                Log.i(ToDoManagerActivity.TAG, "Entered footerView.OnClickListener.onClick()")

                val options: Bundle? = null
                startActivityForResult(
                    mContext as Activity,
                    Intent(
                        mContext,
                        AddToDoActivity::class.java
                    ),
                    ToDoManagerActivity.ADD_TODO_ITEM_REQUEST,
                    options
                )
            }
        }
    }

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

    // Get the ID for the ToDoItem
    // In this case it's just the position

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

        private const val TAG = "Lab-UserInterface"
        private const val HEADER_VIEW_TYPE = R.layout.header_view
        private const val TODO_VIEW_TYPE = R.layout.todo_item

        private lateinit var mNotificationManager: NotificationManager
        private lateinit var mChannelID: String

        private val mVibratePattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
    }


}

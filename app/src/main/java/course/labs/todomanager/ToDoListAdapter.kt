package course.labs.todomanager


//import course.labs.todomanager.ToDoItem.Status
//import android.Manifest
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.format.DateTimeFormatter

class ToDoListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    private val mItems = ArrayList<ToDoItem>()

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

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
        Log.i(TAG, "Adding")
    }

    fun update(item: ToDoItem) {
        for (contact in mItems) {
            if(contact.name == item.name) {
                Log.i(TAG, "First")
                Log.i(TAG, "Contact: " + contact.deadline)
                Log.i(TAG, "Item: " + item.deadline)
                contact.deadline = item.deadline
            }
        }
        notifyDataSetChanged()
    }

    fun delete(item: ToDoItem) {
        for (contact in mItems) {
            if(contact.name == item.name) {
                mItems.remove(contact)
            }
        }
        notifyDataSetChanged()
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

    // Retrieve the number of ToDoItems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
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
            /*viewHolder.mStatusView=v.findViewById(R.id.statusCheckBox) //as CheckBox
            viewHolder.mPriorityView=v.findViewById(R.id.priorityView)*/
//            viewHolder.mTimeLeftView=v.findViewById(R.id.timeLeftView)
            viewHolder.mItemLayout=v


            return viewHolder
        }
    }

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

//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == PERMISSIONS_PICK_CONTACT_REQUEST) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted
//                hasPermission = true
////                startContactsApp()
//                val options: Bundle? = null
//                startActivityForResult(
//                    mContext as Activity,
//                    Intent(
//                        mContext,
//                        AddToDoActivity::class.java
//                    ),
//                    ToDoManagerActivity.ADD_TODO_ITEM_REQUEST,
//                    options
//                )
//            } else {
//                Toast.makeText(
//                    mContext,
//                    "This app requires access to your contact list",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

//    fun checkPermission(): Boolean {
//        Toast.makeText(
//            mContext,
//            "check permission",
//            Toast.LENGTH_SHORT
//        ).show()
////        mContext.checkSelfPermission(mContext)
//        return checkSelfPermission(
//            mContext, READ_CONTACTS_PERM
//        ) == PermissionChecker.PERMISSION_GRANTED
//    }
//
//    fun getPermission() {
//        Toast.makeText(
//            mContext,
//            "Get permission",
//            Toast.LENGTH_SHORT
//        ).show()
//        ActivityCompat.requestPermissions(mContext as Activity, arrayOf(READ_CONTACTS_PERM), PERMISSIONS_PICK_CONTACT_REQUEST)
////        requestPermissions(mContext as Activity, arrayOf(READ_CONTACTS_PERM), PERMISSIONS_PICK_CONTACT_REQUEST)
//    }

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
        var mUpdateView: Button? = null
        var mAddView: FloatingActionButton? = null
    }

    companion object {

        private const val TAG = "Lab-UserInterface"
        private const val HEADER_VIEW_TYPE = R.layout.header_view
        private const val TODO_VIEW_TYPE = R.layout.todo_item

        private var hasPermission: Boolean = false
        private const val PERMISSIONS_PICK_CONTACT_REQUEST = 1
        private const val READ_CONTACTS_PERM = Manifest.permission.READ_CONTACTS
    }


}

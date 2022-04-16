package course.labs.todomanager


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import course.labs.todomanager.ToDoItem.Status

class ToDoListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    private val mItems = ArrayList<ToDoItem>()

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    fun add(item: ToDoItem) {
        mItems.add(item)
        notifyItemChanged(mItems.size)
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
        if (viewType == HEADER_VIEW_TYPE) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.header_view_old, parent, false)
            return ViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.todo_item_old, parent, false)
            val viewHolder = ViewHolder(v)

            // TODO - Inflate the View (defined in todo_item.xml) for this ToDoItem and store references in ViewHolder
            viewHolder.mTitleView=v.findViewById(R.id.titleView)
            viewHolder.mStatusView=v.findViewById(R.id.statusCheckBox) //as CheckBox
            viewHolder.mPriorityView=v.findViewById(R.id.priorityView)
            viewHolder.mDateView=v.findViewById(R.id.dateView)
            viewHolder.mItemLayout=v


                return viewHolder
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        if (position == 0) {
            viewHolder.itemView.setOnClickListener {
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
        } else {
            val toDoItem = mItems[position - 1]

            Log.i(TAG, "onBindViewHolder   " + viewHolder.mTitleView.toString())

            viewHolder.mTitleView!!.text = toDoItem.title

            // Todo - Initialize statusView's isChecked property
            viewHolder.mStatusView!!.isChecked = toDoItem.status==Status.DONE
            // TODO - Set up OnCheckedChangeListener CheckBox
            viewHolder.mStatusView!!.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    toDoItem.status = Status.DONE
                } else {
                    toDoItem.status = Status.NOTDONE
                }
            }
            // TODO - Display Priority in a TextView
            viewHolder.mPriorityView?.text = toDoItem.priority.toString()


            // TODO - Display Time and Date
            viewHolder.mDateView!!.text = ToDoItem.FORMAT.format(
                toDoItem.date
            )

        }
    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    override fun getItemId(pos: Int): Long {
        return pos.toLong() - 1
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemLayout: View = itemView
        var mTitleView: TextView? = null
        var mStatusView: CheckBox? = null
        var mPriorityView: TextView? = null
        var mDateView: TextView? = null
    }

    companion object {
        private const val TAG = "Lab-UserInterface"
        private const val HEADER_VIEW_TYPE = R.layout.header_view_old
        private const val TODO_VIEW_TYPE = R.layout.todo_item_old
    }


}

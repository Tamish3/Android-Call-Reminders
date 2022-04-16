package course.labs.todomanager

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import android.content.Intent

class ToDoItem {

    var title : String? = String()
    var priority : Priority = Priority.LOW
    var status = Status.NOTDONE
    var date = Date()

    enum class Priority {
        LOW, MED, HIGH
    }

    enum class Status {
        NOTDONE, DONE
    }

    internal constructor(title: String, priority: Priority, status: Status, date: Date) {
        this.title = title
        this.priority = priority
        this.status = status
        this.date = date
    }

    // Create a new ToDoItem from data packaged in an Intent

    internal constructor(intent: Intent) {

        title = intent.getStringExtra(TITLE)
        priority = Priority.valueOf(intent.getStringExtra(PRIORITY)!!)
        status = Status.valueOf(intent.getStringExtra(STATUS)!!)

        date = try {
            FORMAT.parse(intent.getStringExtra(DATE)!!)!!
        } catch (e: ParseException) {
            Date()
        }

    }

    override fun toString(): String {
        return (title + ITEM_SEP + priority + ITEM_SEP + status + ITEM_SEP
                + FORMAT.format(date))
    }

    fun toLog(): String {
        return ("Title:" + title + ITEM_SEP + "Priority:" + priority
                + ITEM_SEP + "Status:" + status + ITEM_SEP + "Date:"
                + FORMAT.format(date) + "\n")
    }

    companion object {

        val ITEM_SEP: String? = System.getProperty("line.separator")

        const val TITLE = "title"
        const val PRIORITY = "priority"
        const val STATUS = "status"
        const val DATE = "date"
//        val FILENAME = "filename"

        val FORMAT = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US)

        // Take a set of String data values and
        // package them for transport in an Intent

        fun packageIntent(intent: Intent, title: String,
                          priority: Priority, status: Status, date: String) {

            intent.putExtra(TITLE, title)
            intent.putExtra(PRIORITY, priority.toString())
            intent.putExtra(STATUS, status.toString())
            intent.putExtra(DATE, date)

        }
    }

}

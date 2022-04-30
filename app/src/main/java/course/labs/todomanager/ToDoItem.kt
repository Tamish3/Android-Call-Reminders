package course.labs.todomanager

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import android.content.Intent
import java.sql.Time
import java.time.ZonedDateTime

class ToDoItem {

    var title : String? = String()
//    var priority : Priority = Priority.LOW
//    var status = Status.NOTDONE
    var date = Date()

    var name : String? = String()
    var time : String? = String()
    /*
    enum class Priority {
        LOW, MED, HIGH
    }

    enum class Status {
        NOTDONE, DONE
    }
    */
    internal constructor(name: String) {
//        this.title = title
//        this.priority = priority
//        this.status = status
//        this.date = date

        this.name = name
//        this.time = time
    }

    // Create a new ToDoItem from data packaged in an Intent

    internal constructor(intent: Intent) {

        name = intent.getStringExtra(NAME)
//        title = intent.getStringExtra(TITLE)
//        priority = Priority.valueOf(intent.getStringExtra(PRIORITY)!!)
//        status = Status.valueOf(intent.getStringExtra(STATUS)!!)

//        date = try {
//            FORMAT.parse(intent.getStringExtra(DATE)!!)!!
//        } catch (e: ParseException) {
//            Date()
//        }

    }

    override fun toString(): String {
        return (name + ITEM_SEP)
//                + FORMAT.format(date))
    }

    fun toLog(): String {
        return("Name:" + name + "Time:")
    }

    companion object {

        val ITEM_SEP: String? = System.getProperty("line.separator")

        const val NAME = "name"
        const val TIME = "time"
//        val FILENAME = "filename"

//        val FORMAT = SimpleTimeFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.US)

        // Take a set of String data values and
        // package them for transport in an Intent

        fun packageIntent(intent: Intent, name: String, deadline : ZonedDateTime) {
//                          priority: Priority, status: Status, date: String) {

            intent.putExtra(NAME, name)
//            intent.putExtra(PRIORITY, priority.toString())
//            intent.putExtra(STATUS, status.toString())
//            intent.putExtra(DATE, date)

        }
    }

}

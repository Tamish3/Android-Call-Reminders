package course.labs.todomanager

import android.content.Intent
import java.time.Duration
import java.time.Period
import java.time.ZonedDateTime
import java.util.*

class ToDoItem {

    var title : String? = String()
//    var priority : Priority = Priority.LOW
//    var status = Status.NOTDONE
    var date = Date()

    var name : String? = String();
    var deadline : ZonedDateTime? = null;
    var dateRange : Period? = null;
    var timeRange : Duration? = null;
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

        name = intent.getStringExtra("name")
        deadline = intent.getSerializableExtra("deadline") as ZonedDateTime
        dateRange = intent.getSerializableExtra("dateRange") as Period
        timeRange = intent.getSerializableExtra("timeRange") as Duration
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

        // Take a set of String data values and
        // package them for transport in an Intent

        fun packageIntent(intent: Intent, name: String, deadline: ZonedDateTime, dateRange: Period, timeRange: Duration) {
            intent.putExtra("name", name)
            intent.putExtra("deadline", deadline)
            intent.putExtra("dateRange", dateRange)
            intent.putExtra("timeRange", timeRange)
        }
    }

}

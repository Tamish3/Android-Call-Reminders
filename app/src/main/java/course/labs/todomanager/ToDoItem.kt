package course.labs.todomanager

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import java.time.Duration
import java.time.Period
import java.time.ZonedDateTime
import java.util.*

class ToDoItem {

    var title : String? = String()
//    var priority : Priority = Priority.LOW
//    var status = Status.NOTDONE
    var date = Date()

    var icon : String? = String();
    var name : String? = String();
    public var deadline : ZonedDateTime? = null;
    var phoneNumber : String? = String();
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
    internal constructor(icon: String, name: String, deadline: ZonedDateTime, phoneNumber: String, dateRange: Period, timeRange: Duration) {
        this.icon = icon
        this.name = name
        this.deadline = deadline
        this.phoneNumber = phoneNumber
        this.dateRange = dateRange
        this.timeRange = timeRange
    }

    // Create a new ToDoItem from data packaged in an Intent

    internal constructor(intent: Intent) {
        icon = intent.getStringExtra("icon")
        name = intent.getStringExtra("name")
        deadline = intent.getSerializableExtra("deadline") as ZonedDateTime
        phoneNumber = intent.getStringExtra("phoneNumber")
        dateRange = intent.getSerializableExtra("dateRange") as Period?
        timeRange = intent.getSerializableExtra("timeRange") as Duration?

    }

    override fun toString(): String {
        return (icon + ITEM_SEP + name + ITEM_SEP + deadline.toString() + ITEM_SEP + phoneNumber + ITEM_SEP + dateRange.toString() + ITEM_SEP + timeRange.toString() + ITEM_SEP)
//                + FORMAT.format(date))
    }

    fun toLog(): String {
        return("Name:" + name + "Time:")
    }

    companion object {

        val ITEM_SEP: String? = System.getProperty("line.separator")

        // Take a set of String data values and
        // package them for transport in an Intent

        fun packageIntent(
            intent: Intent,
            icon: String?,
            name: String,
            deadline: ZonedDateTime?,
            phoneNumber: String?,
            dateRange: Period?,
            timeRange: Duration?
        ) {
            intent.putExtra("icon", icon)
            intent.putExtra("name", name)
            intent.putExtra("deadline", deadline)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("dateRange", dateRange)
            intent.putExtra("timeRange", timeRange)
        }
    }

}

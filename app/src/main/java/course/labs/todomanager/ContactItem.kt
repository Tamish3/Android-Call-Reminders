package course.labs.todomanager

import android.content.Intent
import java.time.Duration
import java.time.Period
import java.time.ZonedDateTime

/**
 * Structure of the contact that is being stored consisting of seven
 * different componenets
 */
class ContactItem {

    var icon : String? = String();
    var name : String? = String();
    var deadline : ZonedDateTime? = null;
    var phoneNumber : String? = String();
    var oldTime : ZonedDateTime? = null;
    var dateRange : Period? = null;
    var timeRange : Duration? = null;

    //Contact Constructor
    internal constructor(icon: String, name: String, deadline: ZonedDateTime, phoneNumber: String, oldTime: ZonedDateTime, dateRange: Period, timeRange: Duration) {
        this.icon = icon
        this.name = name
        this.deadline = deadline
        this.phoneNumber = phoneNumber
        this.oldTime = oldTime
        this.dateRange = dateRange
        this.timeRange = timeRange
    }

    // Create a new ContactItem from data that is packaged in an Intent
    internal constructor(intent: Intent) {
        icon = intent.getStringExtra("icon")
        name = intent.getStringExtra("name")
        deadline = intent.getSerializableExtra("deadline") as ZonedDateTime
        phoneNumber = intent.getStringExtra("phoneNumber")
        oldTime = intent.getSerializableExtra("oldTime") as ZonedDateTime
        dateRange = intent.getSerializableExtra("dateRange") as Period?
        timeRange = intent.getSerializableExtra("timeRange") as Duration?

    }

    //ToString for the ContactItem
    override fun toString(): String {
        return (icon + ITEM_SEP + name + ITEM_SEP + deadline.toString()
                + ITEM_SEP + phoneNumber + ITEM_SEP + oldTime.toString()
                + ITEM_SEP + dateRange.toString() + ITEM_SEP
                + timeRange.toString() + ITEM_SEP)
    }

    companion object {
        val ITEM_SEP: String? = System.getProperty("line.separator")

        fun packageIntent(
            intent: Intent,
            icon: String?,
            name: String,
            deadline: ZonedDateTime?,
            phoneNumber: String?,
            oldTime: ZonedDateTime?,
            dateRange: Period?,
            timeRange: Duration?
        ) {
            intent.putExtra("icon", icon)
            intent.putExtra("name", name)
            intent.putExtra("deadline", deadline)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("oldTime", oldTime)
            intent.putExtra("dateRange", dateRange)
            intent.putExtra("timeRange", timeRange)
        }
    }
}

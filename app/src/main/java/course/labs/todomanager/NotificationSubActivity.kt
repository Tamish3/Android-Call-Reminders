package course.labs.todomanager

import android.app.Activity
import android.os.Bundle

/**
 * this is for the Notification subactivity a helper for the
 * NotificationListener
 */
class NotificationSubActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sub_activity)
    }
}

package course.labs.todomanager


import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import course.labs.todomanager.ContactManagerActivity.Companion.mAdapter

/**
 * NotificationListener Service that listens to all notification from the phone
 * and does updateTo when it gets a Notification from the Call Your Mother app
 */
class NotificationListener : NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    //When notification is posted makes sure it is a notification form the
    //CallyourMother App and then calls the updateTo function in ContactListAdapter
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName.equals("course.labs.todomanager")) {
            mAdapter?.updateTo(sbn.notification.tickerText.toString())
        }
    }
}

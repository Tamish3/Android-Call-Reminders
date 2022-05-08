package course.labs.todomanager


import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import course.labs.todomanager.ToDoManagerActivity.Companion.mAdapter


class NotificationListener : NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName.equals("course.labs.todomanager")) {
            mAdapter?.updateTo(sbn.notification.tickerText.toString())
        }
    }
}

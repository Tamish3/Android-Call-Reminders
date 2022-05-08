package course.labs.todomanager


import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import course.labs.todomanager.ToDoManagerActivity.Companion.mAdapter


class NotificationListener : NotificationListenerService() {
    private val WAKE_TIME = 5000L

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.i("NotificationListener", "** onNotificationPosted")
        Log.i(
            "NotificationListener",
            "ID :" + sbn.id + " \t " + sbn.notification.tickerText + " \t " + sbn.packageName
        )

        if (sbn.packageName.equals("course.labs.todomanager")) {
            mAdapter?.updateTo(sbn.notification.tickerText.toString())
        }
    }

//    @SuppressLint("InvalidWakeLockTag")
//    override fun onNotificationPosted(statusBarNotification: StatusBarNotification?) {

//        val km = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
//        if (km.inKeyguardRestrictedInputMode()) {
//            val powerManager = getSystemService(POWER_SERVICE) as PowerManager
//            if (!powerManager.isPowerSaveMode) {
//                val wakeLock = powerManager.newWakeLock(
//                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
//                    "ScreenNotificationsLock"
//                )
//                wakeLock.acquire(WAKE_TIME)
//            }
//        }
//        super.onNotificationPosted(statusBarNotification)
//    }

}

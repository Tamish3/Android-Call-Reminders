package course.labs.todomanager

import android.R
import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

//https://stackoverflow.com/questions/34517520/how-to-give-notifications-on-android-on-specific-time
class MyNewIntentService : IntentService("MyNewIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        val builder: Notification.Builder = Notification.Builder(this)
        builder.setContentTitle("My Title")
        builder.setContentText("This is the Body")
        val notifyIntent = Intent(this, NotificationSubActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent)
        val notificationCompat: Notification = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)
    }

    companion object {
        private const val NOTIFICATION_ID = 3

    }
}


package course.labs.todomanager

//import android.R
import android.R.drawable
import android.app.AlarmManager
import course.labs.todomanager.R
import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity

//https://stackoverflow.com/questions/34517520/how-to-give-notifications-on-android-on-specific-time
class NotificationIntentService : IntentService("MyNewIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        mChannelID = "$packageName.channel_01"
        val builder: Notification.Builder = Notification.Builder(applicationContext
            , mChannelID
        )

        builder.setContentTitle("Call Your Mother Notification")
        builder.setContentText("Call your contact: " + intent?.getStringExtra("name"))
        builder.setSmallIcon(course.labs.todomanager.R.drawable.ic_stat_name)
        builder.setTicker(intent?.getStringExtra("name"))

        val notifyIntent = Intent(this, NotificationSubActivity::class.java)
        val pendingIntent =
//            PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_IMMUTABLE)
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent)
        val notificationCompat: Notification = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)


    }

    companion object {
        private const val NOTIFICATION_ID = 3
        private lateinit var mChannelID: String
    }
}


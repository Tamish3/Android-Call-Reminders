package course.labs.todomanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReciever for the Notification Reciever and when
 * a broadcast is recieved the Service is started with an intent
 */
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intent1 = Intent(context, NotificationIntentService::class.java)
        val name = intent.getStringExtra("name")

        intent1.putExtra("name", name)
        context.startService(intent1)
    }

}

package course.labs.todomanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intent1 = Intent(context, NotificationIntentService::class.java)
        val name = intent.getStringExtra("name")

        intent1.putExtra("name", name)
        context.startService(intent1)
    }

}

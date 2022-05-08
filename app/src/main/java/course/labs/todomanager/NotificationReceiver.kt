package course.labs.todomanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intent1 = Intent(context, MyNewIntentService::class.java)
        val name = intent.getStringExtra("name")
        if (name != null) {
            Log.i("tag", name)
        }
        intent1.putExtra("name", name)
        context.startService(intent1)
    }
}

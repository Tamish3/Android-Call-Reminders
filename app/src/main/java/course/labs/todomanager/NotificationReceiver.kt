package course.labs.todomanager

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.util.Log


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intent1 = Intent(context, MyNewIntentService::class.java)
        val name = intent.getStringExtra("name")

        intent1.putExtra("name", name)
        context.startService(intent1)
    }

}

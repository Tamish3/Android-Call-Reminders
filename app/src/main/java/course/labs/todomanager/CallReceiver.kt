package course.examples.broadcastreceiver.singlebroadcaststaticregistration

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class CallReceiver : BroadcastReceiver() {
    companion object {
        internal const val TAG = "Receiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Broadcast Received")
        Toast.makeText(context, "Broadcast Received by Receiver", Toast.LENGTH_LONG).show()

        //A contact calls you
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals("")) {

            }
        }
        //You call a contact
        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals("")) {

            }
        }

    }
}

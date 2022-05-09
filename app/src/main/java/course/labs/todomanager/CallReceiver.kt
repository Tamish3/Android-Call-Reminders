package course.labs.todomanager

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import java.time.ZonedDateTime


/** Looks for if any calls recieved or calls made are from/to
 * a contact that has been added by the user to keep track off
 * Source: https://www.youtube.com/watch?v=rlzfcqDlovg
 * https://stackoverflow.com/questions/1853220/retrieve-incoming-calls-phone-number-in-android?noredirect=1&lq=1
 */
class CallReceiver : BroadcastReceiver() {
    companion object {
        internal const val TAG = "Receiver"
    }
    private lateinit var adapter: ContactListAdapter;
    //https://www.youtube.com/watch?v=rlzfcqDlovg
    //https://stackoverflow.com/questions/1853220/retrieve-incoming-calls-phone-number-in-android?noredirect=1&lq=1
    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (incomingNumber != null) {
                if (ContactManagerActivity.mAdapter != null) {
                    adapter = ContactManagerActivity.mAdapter!!
                    updateDeadline(incomingNumber, context)
                }
            } //We can condense later, code is the same
        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            val outgoingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (outgoingNumber != null) {
                if (ContactManagerActivity.mAdapter != null) {
                    adapter = ContactManagerActivity.mAdapter!!
                    updateDeadline(outgoingNumber, context)
                }
            }
        }
    }

    //https://stackoverflow.com/questions/26192302/get-the-name-of-the-incoming-caller-before-programmatically-ending-the-call
    //update the deadline when a call is received or made to a contact being
    //tracked of and part of ContactListAdapter
    @SuppressLint("NewApi")
    private fun updateDeadline(number: String, context: Context) : Unit {
        for (i in 1 until adapter.itemCount) {
            var item : ContactItem = adapter.getItem(i) as ContactItem
            if (PhoneNumberUtils.areSamePhoneNumber(item.phoneNumber.toString(), number, "us")) {
                item.deadline = ZonedDateTime.now().plus(item.dateRange).plus(item.timeRange)
                adapter.notifyDataSetChanged();
            }
        }
    }
}

package course.labs.todomanager

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import java.time.ZonedDateTime


class CallReceiver : BroadcastReceiver() {
    companion object {
        internal const val TAG = "Receiver"

    }
    private lateinit var adapter: ToDoListAdapter;
    //https://www.youtube.com/watch?v=rlzfcqDlovg
    //https://stackoverflow.com/questions/1853220/retrieve-incoming-calls-phone-number-in-android?noredirect=1&lq=1
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Broadcast Received")
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (incomingNumber != null) {
                Log.i(TAG, "$incomingNumber")
                if (ToDoManagerActivity.mAdapter != null) {
                    adapter = ToDoManagerActivity.mAdapter!!
                    updateDeadline(incomingNumber, context)
                }
            } //We can condense later, code is the same
        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            val outgoingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            Log.i(TAG, "$outgoingNumber")
            if (outgoingNumber != null) {
                Log.i(TAG, "$outgoingNumber")
                if (ToDoManagerActivity.mAdapter != null) {
                    adapter = ToDoManagerActivity.mAdapter!!
                    updateDeadline(outgoingNumber, context)
                }
            }
        }
    }

    //https://stackoverflow.com/questions/26192302/get-the-name-of-the-incoming-caller-before-programmatically-ending-the-call
    @SuppressLint("Range")
    private fun getContactName(number: String, context: Context) : String{
        var contactName = "";

        // // define the columns I want the query to return
        val projection = arrayOf(
            ContactsContract.PhoneLookup.DISPLAY_NAME,
            ContactsContract.PhoneLookup.NUMBER,
            ContactsContract.PhoneLookup.HAS_PHONE_NUMBER )

        // encode the phone number and build the filter URI
        val contactUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
        Uri.encode(number));

        // query time
        val cursor = context.contentResolver.query(contactUri,
            projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

        }
        cursor?.close();
        return contactName
    }

    @SuppressLint("NewApi")
    private fun updateDeadline(number: String, context: Context) : Unit {
        for (i in 1 until adapter.itemCount) {
            var item : ToDoItem = adapter.getItem(i) as ToDoItem
            if (PhoneNumberUtils.areSamePhoneNumber(item.phoneNumber.toString(), number, "us")) {
                item.deadline = item.deadline!!.plus(item.dateRange).plus(item.timeRange)
                adapter.notifyDataSetChanged();
            }
        }
    }
}

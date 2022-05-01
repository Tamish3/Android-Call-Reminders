package course.labs.todomanager

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import java.util.*


class CallReceiver : BroadcastReceiver() {
    companion object {
        internal const val TAG = "Receiver"

    }
//
//    var lastcall = Date()
//    var phonenum = "+16505556789"

    //https://www.youtube.com/watch?v=rlzfcqDlovg
    //https://stackoverflow.com/questions/1853220/retrieve-incoming-calls-phone-number-in-android?noredirect=1&lq=1
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Broadcast Received")
//        Toast.makeText(context, "Broadcast Received by Receiver", Toast.LENGTH_LONG).show()
//        Toast.makeText(context, getContactName(phonenum, context), Toast.LENGTH_LONG).show()
//        Toast.makeText(context, TelephonyManager.EXTRA_INCOMING_NUMBER, Toast.LENGTH_LONG).show()

        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        if (incomingNumber != null) {
            Log.i(TAG, "$incomingNumber")
            Toast.makeText(context, getContactName(incomingNumber, context), Toast.LENGTH_LONG).show()
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
        val cursor = context.getContentResolver().query(contactUri,
            projection, null, null, null);
        // querying all contacts = Cursor cursor =
        // context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
        // projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }
        }
        if (cursor != null) {
            cursor.close()
        };
        return contactName

    }

}

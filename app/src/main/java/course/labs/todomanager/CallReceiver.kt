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

    var lastcall = Date()
    var phonenum = "(650) 555-6789"

    //https://www.youtube.com/watch?v=rlzfcqDlovg
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Broadcast Received")
//        Toast.makeText(context, "Broadcast Received by Receiver", Toast.LENGTH_LONG).show()
//        Toast.makeText(context, getContactName(phonenum, context), Toast.LENGTH_LONG).show()
//        Toast.makeText(context, TelephonyManager.EXTRA_INCOMING_NUMBER, Toast.LENGTH_LONG).show()


        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
//            Toast.makeText(context, "Incoming Call State", Toast.LENGTH_SHORT).show()
//            Toast.makeText(
//                context,
//                "Ringing State Number is -$incomingNumber",
//                Toast.LENGTH_SHORT
//            ).show()
        }
        if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
//            Toast.makeText(context, "Call Received State", Toast.LENGTH_SHORT).show()
        }
//        Toast.makeText(
//                context,
//                "Ringing State Number is -$incomingNumber",
//                Toast.LENGTH_SHORT
//            ).show()
        if (incomingNumber != null) {
            Log.i(TAG, "$incomingNumber")
        }


        //name, id
        //A contact calls you
//        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//            Toast.makeText(context, intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER), Toast.LENGTH_LONG).show()
//            if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(phonenum)) {
//
//
//            }
//            val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            telephony.listen(object : PhoneStateListener() {
//                override fun onCallStateChanged(state: Int, incomingNumber: String) {
//                    super.onCallStateChanged(state, incomingNumber)
//                    println("incomingNumber : $incomingNumber")
//                    Toast.makeText(context, "$incomingNumber", Toast.LENGTH_LONG).show()
//                }
//            }, PhoneStateListener.LISTEN_CALL_STATE)

//        }
        //You call a contact
//        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//            if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(phonenum)) {
//
//            }
//        }


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

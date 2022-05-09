package course.labs.todomanager


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.FragmentActivity
import java.time.*
import java.time.temporal.ChronoUnit


class AddContactActivity : FragmentActivity() {
    private lateinit var nameView: TextView
    private lateinit var iconView: ImageView
    private lateinit var contactIcon : String;
    private lateinit var phoneNumber : String;

    var years: Int = 0
    var months: Int = 0
    var weeks: Int = 0
    var days: Int = 0
    var hours: Int = 0
    var minutes: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact)
        nameView = findViewById<TextView>(R.id.contactNameView)
        iconView = findViewById<ImageView>(R.id.contactIconView)

        val button = findViewById<Button>(R.id.contactsButton)
        button.setOnClickListener {
            pickContactIntent()
        }

        val dropdown = findViewById<Spinner>(R.id.spinner)
        val items = arrayOf("1 day", "1 week", "1 month", "6 months", "1 year", "Custom")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter

        val custom = findViewById<TextView>(R.id.custom)
        val customLayout = findViewById<View>(R.id.custom_layout) as LinearLayout
        custom.visibility = View.GONE
        customLayout.visibility = View.GONE

        dropdown.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                var value = dropdown.selectedItem.toString()

                years = 0
                months = 0
                weeks= 0
                days = 0
                hours= 0
                minutes = 0

                if (value == "Custom") {
                    val custom = findViewById<View>(R.id.custom_layout) as LinearLayout
                    custom.visibility = View.VISIBLE
                    customLayout.visibility = View.VISIBLE
                } else if (value == "1 day") {
                    days = 1;
                    custom.visibility = View.GONE
                    customLayout.visibility = View.GONE
                } else if (value == "1 week") {
                    weeks = 1;
                    custom.visibility = View.GONE
                    customLayout.visibility = View.GONE
                } else if (value == "1 month") {
                    months = 1;
                    custom.visibility = View.GONE
                    customLayout.visibility = View.GONE
                } else if (value == "6 months") {
                    months = 6;
                    custom.visibility = View.GONE
                    customLayout.visibility = View.GONE
                } else if (value == "1 year") {
                    years = 1;
                    custom.visibility = View.GONE
                    customLayout.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })

        var yearsTxt = findViewById<EditText>(R.id.years)
        var monthsTxt = findViewById<EditText>(R.id.months)
        var weeksTxt = findViewById<EditText>(R.id.weeks)
        var daysTxt = findViewById<EditText>(R.id.days)
        var hoursTxt = findViewById<EditText>(R.id.hours)
        var minutesTxt = findViewById<EditText>(R.id.minutes)

        yearsTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().toIntOrNull() != null) {
                    years = s.toString().toInt()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        monthsTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().toIntOrNull() != null) {
                    months = s.toString().toInt()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        weeksTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().toIntOrNull() != null) {
                    weeks = s.toString().toInt()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        daysTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().toIntOrNull() != null) {
                    days = s.toString().toInt()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        hoursTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().toIntOrNull() != null) {
                    hours = s.toString().toInt()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        minutesTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().toIntOrNull() != null) {
                    minutes = s.toString().toInt()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
//          actionBar = setActionBar()
//        actionBar!!.title = "Add New Contact";
        val cancelButton = findViewById<View>(R.id.cancelButton) as Button
        cancelButton.setOnClickListener {

            // TODO - Indicate result and finish
            setResult(RESULT_CANCELED)
            finish()
        }

        // TODO - Set up OnClickListener for the Reset Button
        val resetButton = findViewById<View>(R.id.resetButton) as Button
        resetButton.setOnClickListener {
            Log.i(TAG, "Entered resetButton.OnClickListener.onClick()")

            nameView.text = ""
            iconView.setImageResource(0)
            dropdown.setSelection(0)
        }

        //notification channel
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        // Set up OnClickListener for the Submit Button

        val submitButton = findViewById<View>(R.id.submitButton) as Button
        submitButton.setOnClickListener {
            Log.i(TAG, "Entered submitButton.OnClickListener.onClick()")
            var data = Intent()

            var dateRange = Period.of(years, months, days + 7*weeks)
            var timeRange = Duration.of(hours.toLong(), ChronoUnit.HOURS)
            timeRange = timeRange.plus(minutes.toLong(), ChronoUnit.MINUTES)
            var currentTime = ZonedDateTime.now()
            var deadline = currentTime.plus(dateRange).plus(timeRange)

            val name = nameView.text.toString()
            if(name == "") {
                Toast.makeText(applicationContext,
                    "You need to choose a contact!",
                    Toast.LENGTH_SHORT
                ).show()
                pickContactIntent()
            }
            else {

                ContactItem.packageIntent(
                    data,
                    contactIcon,
                    name,
                    deadline,
                    phoneNumber,
                    currentTime,
                    dateRange,
                    timeRange
                )

                // TODO - return data Intent and finish
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }

    private fun createNotificationChannel() {
        mChannelID = "$packageName.channel_01"

        // The user-visible name of the channel.
        val name = getString(R.string.channel_name)

        // The user-visible description of the channel
        val description = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelID, name, importance)

        // Configure the notification channel.
        mChannel.description = description
        mChannel.enableLights(true)

        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)

        mNotificationManager.createNotificationChannel(mChannel)
    }


    fun pickContactIntent() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT_REQUEST)
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Ensure that this call is the result of a successful PICK_CONTACT_REQUEST request
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_CONTACT_REQUEST) {
            val contactUri = data!!.data
            val projection = null
            val cr = contentResolver
            val cursor = cr.query(contactUri!!, projection, null, null, null)

            if (cursor!!.moveToFirst()) {
                val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)) != null) {
                    contactIcon = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                } else {
                    contactIcon = "";
                }
                val phoneResults = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))

                if (phoneResults == 1) {
                    val cursor2 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+contactId,
                        null,
                        null
                    )



                    if (cursor2 != null) {
                        var contactNumber = "";
                        while(cursor2.moveToNext()) {
                            if (contactNumber == "") {
                                contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            }
                        }

                        nameView.text = contactName.toString()
                        phoneNumber = contactNumber;
                        if (contactIcon != "") {
                            iconView.setImageURI(Uri.parse(contactIcon))
                        } else {
                            iconView.setImageDrawable(getDrawable(R.drawable.ic_account_circle))
                        }
                    }
                    cursor2?.close()
                }


            }
//            }
            cursor?.close()
        }
    }

    companion object {
        private const val TAG = "Lab-UserInterface"
        private const val PICK_CONTACT_REQUEST = 0
        private const val PERMISSIONS_PICK_CONTACT_REQUEST = 1
        private lateinit var mNotificationManager: NotificationManager
        private lateinit var mChannelID: String

    }
}

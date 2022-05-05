package course.labs.todomanager

//import course.labs.todomanager.ToDoItem.Priority
//import course.labs.todomanager.ToDoItem.Status

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*


class AddToDoActivity : FragmentActivity() {

    private lateinit var mDate: Date
    private lateinit var mPriorityRadioGroup: RadioGroup
    private lateinit var mStatusRadioGroup: RadioGroup
    private lateinit var mTitleText: EditText
    private lateinit var mDefaultStatusButton: RadioButton
    private lateinit var mDefaultPriorityButton: RadioButton
    private lateinit var dateView: TextView
    private lateinit var timeView: TextView
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
        setContentView(R.layout.add_todo_new)
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

//        actionBar!!.title = "Add New Contact";
        val cancelButton = findViewById<View>(R.id.cancelButton) as Button
        cancelButton.setOnClickListener {
            Log.i(TAG, "Entered cancelButton.OnClickListener.onClick()")

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

            Toast.makeText(applicationContext,
                "$name will be notified at $deadline.toString()",
                Toast.LENGTH_SHORT
            ).show()

            ToDoItem.packageIntent(data, contactIcon, name, deadline, phoneNumber, dateRange, timeRange)

            // TODO - return data Intent and finish
            setResult(RESULT_OK, data)
            finish()
        }
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
            Toast.makeText(
                this,
                "Entered first if",
                Toast.LENGTH_SHORT
            ).show()
            // These details are covered in the lesson on ContentProviders
//            val contactUri = data?.data ?: return
            val contactUri = data!!.data
            val projection = null
            val cr = contentResolver
            val cursor = cr.query(contactUri!!, projection, null, null, null)


            //name, icon, phone num, contact id
//            if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)) != null) {
                    contactIcon = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                } else {
                    contactIcon = "";
                }
                val phoneResults = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))

                Toast.makeText(
                    this,
                    "Got Id$contactId",
                    Toast.LENGTH_SHORT
                ).show()

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
                            iconView?.setImageDrawable(getDrawable(R.drawable.ic_account_circle))
                        }
                    }
                    cursor2?.close()
                }
//                    var textHelper = findViewById<TextView>(R.id.contactNameView)
//                    textHelper.text = contactName.toString()
//
//                    var iconHelper = findViewById<ImageView>(R.id.contactIconView)
////                    iconHelper.setImageURI()

            }
//            }
            cursor?.close()
        }
    }

//    private fun setDefaultDateTime() {
//
//        // Default is current time + 7 days
//        mDate = Date()
//        mDate = Date(mDate.time + SEVEN_DAYS)
//
//        val c = Calendar.getInstance()
//        c.time = mDate
//
//        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
//                c.get(Calendar.DAY_OF_MONTH))
//
//        dateView.text = dateString
//
//        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
//
//        timeView.text = timeString
//    }

    // DialogFragment used to pick a ToDoItem deadline date

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                               dayOfMonth: Int) {
            setDateString(year, monthOfYear, dayOfMonth)
            val dateView: TextView = requireActivity().findViewById(R.id.date)
            dateView.text = dateString
        }
    }

    // DialogFragment used to pick a ToDoItem deadline time

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return
            return TimePickerDialog(activity, this, hour, minute, true)
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            setTimeString(hourOfDay, minute)
            val timeView: TextView = requireActivity().findViewById(R.id.time)
            timeView.text = timeString
        }
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun showTimePickerDialog() {
        val newFragment = TimePickerFragment()
        newFragment.show(supportFragmentManager, "timePicker")
    }

    companion object {

        // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
        private const val SEVEN_DAYS = 604800000

        private const val TAG = "Lab-UserInterface"

        private var timeString: String? = null
        private var dateString: String? = null

        private fun setDateString(year: Int, monthOfYear: Int, dayOfMonth: Int) {
            var localMonthOfYear = monthOfYear

            // Increment monthOfYear for Calendar/Date -> Time Format setting
            localMonthOfYear++
            var mon = "" + localMonthOfYear
            var day = "" + dayOfMonth

            if (localMonthOfYear < 10)
                mon = "0$localMonthOfYear"
            if (dayOfMonth < 10)
                day = "0$dayOfMonth"

            dateString = "$year-$mon-$day"
        }

        private fun setTimeString(hourOfDay: Int, minute: Int) {
            var hour = "" + hourOfDay
            var min = "" + minute

            if (hourOfDay < 10)
                hour = "0$hourOfDay"
            if (minute < 10)
                min = "0$minute"

            timeString = "$hour:$min:00"
        }

        private const val PICK_CONTACT_REQUEST = 0
        private const val PERMISSIONS_PICK_CONTACT_REQUEST = 1
//        private const val FORMATTED_ADDRESS =
//            ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS
    }
}

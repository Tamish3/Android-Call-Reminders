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


class UpdateToDoActivity : FragmentActivity() {

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
        setContentView(R.layout.update)

        val dropdown = findViewById<Spinner>(R.id.updatespinner)
        val items = arrayOf("1 day", "1 week", "1 month", "6 months", "1 year", "Custom")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter

        val custom = findViewById<TextView>(R.id.updatecustom)
        val customLayout = findViewById<View>(R.id.updatecustom_layout) as LinearLayout
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

        var yearsTxt = findViewById<EditText>(R.id.updateyears)
        var monthsTxt = findViewById<EditText>(R.id.updatemonths)
        var weeksTxt = findViewById<EditText>(R.id.updateweeks)
        var daysTxt = findViewById<EditText>(R.id.updatedays)
        var hoursTxt = findViewById<EditText>(R.id.updatehours)
        var minutesTxt = findViewById<EditText>(R.id.updateminutes)

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

        //actionBar!!.title = "Add New Contact";
        val cancelButton = findViewById<View>(R.id.updatecancelButton) as Button
        cancelButton.setOnClickListener {
            Log.i(TAG, "Entered cancelButton.OnClickListener.onClick()")

            // TODO - Indicate result and finish
            setResult(RESULT_CANCELED)
            finish()
        }

//        // TODO - Set up OnClickListener for the Reset Button
//        val resetButton = findViewById<View>(R.id.resetButton) as Button
//        resetButton.setOnClickListener {
//            Log.i(TAG, "Entered resetButton.OnClickListener.onClick()")
//
//            nameView.text = ""
//            iconView.setImageResource(0)
//            dropdown.setSelection(0)
//        }

        // Set up OnClickListener for the Submit Button

        val submitButton = findViewById<View>(R.id.updatesubmitButton) as Button
        submitButton.setOnClickListener {
            Log.i(TAG, "Entered submitButton.OnClickListener.onClick()")
            var data = Intent()

            var dateRange = Period.of(years, months, days + 7*weeks)
            var timeRange = Duration.of(hours.toLong(), ChronoUnit.HOURS)
            timeRange = timeRange.plus(minutes.toLong(), ChronoUnit.MINUTES)
            var currentTime = ZonedDateTime.now()
            var deadline = currentTime.plus(dateRange).plus(timeRange)

            val name = intent.getStringExtra("name")
            val icon = intent.getStringExtra("icon")
            val phoneNumber = intent.getStringExtra("phoneNumber")

            Toast.makeText(applicationContext,
                "$name will be notified at $deadline.toString()",
                Toast.LENGTH_SHORT
            ).show()

            if (name != null) {
                ToDoItem.packageIntent(data, icon, name, deadline, phoneNumber, dateRange, timeRange)
            }

            // TODO - return data Intent and finish
            setResult(RESULT_OK, data)
            finish()
        }
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

    }
}

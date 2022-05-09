package course.labs.todomanager

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.FragmentActivity
import java.time.*
import java.time.temporal.ChronoUnit

/** Takes care of changing the next reminder for a contact or deleting
 * the user */
class UpdateContactActivity : FragmentActivity() {

    var years: Int = 0
    var months: Int = 0
    var weeks: Int = 0
    var days: Int = 0
    var hours: Int = 0
    var minutes: Int = 0

    //Oncreat function takes care of getting all the input from the user
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

        //getting info from a normal dropdown selector
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
                    val custom = findViewById<View>(R.id.updatecustom_layout) as LinearLayout
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

        //storing the time info from custom
        var yearsTxt = findViewById<EditText>(R.id.updateyears)
        var monthsTxt = findViewById<EditText>(R.id.updatemonths)
        var weeksTxt = findViewById<EditText>(R.id.updateweeks)
        var daysTxt = findViewById<EditText>(R.id.updatedays)
        var hoursTxt = findViewById<EditText>(R.id.updatehours)
        var minutesTxt = findViewById<EditText>(R.id.updateminutes)

        //gets the information if the user selects a custom input
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


        //exits the activity of cancel_button is pressed
        val cancelButton = findViewById<View>(R.id.updatecancelButton) as Button
        cancelButton.setOnClickListener {
            Log.i(TAG, "Entered cancelButton.OnClickListener.onClick()")

            setResult(RESULT_CANCELED)
            finish()
        }

        //the delete button deletes the contact by sending the contact info back
        //to the ContactManagerActivity which calls the delete function in the
        //Contact List Adapter on this contact
        val deleteButton = findViewById<View>(R.id.updatedeleteButton) as Button
        deleteButton.setOnClickListener {
            Log.i(TAG, "Entered deleteButton.OnClickListener.onClick()")

            var data = Intent()

            var dateRange = intent.getSerializableExtra("dateRange") as Period
            var timeRange = intent.getSerializableExtra("timeRange") as Duration
            var oldTime = intent.getSerializableExtra("oldTime") as ZonedDateTime
            var deadline = oldTime.plus(dateRange).plus(timeRange)

            val name = intent.getStringExtra("name")
            val icon = intent.getStringExtra("icon")
            val phoneNumber = intent.getStringExtra("phoneNumber")

            if (name != null) {
                ContactItem.packageIntent(data, icon, name, deadline, phoneNumber, oldTime, dateRange, timeRange)
            }

            // return data Intent and the finish
            setResult(RESULT_FIRST_USER, data)
            finish()
        }

        // Set up OnClickListener for the Submit Button
        //the submit button updates the contact by sending the contact info back
        //to the ContactManagerActivity which calls the update function in the
        //Contact List Adapter on this contact
        val submitButton = findViewById<View>(R.id.updatesubmitButton) as Button
        submitButton.setOnClickListener {
            Log.i(TAG, "Entered submitButton.OnClickListener.onClick()")
            var data = Intent()

            var dateRange = Period.of(years, months, days + 7*weeks)
            var timeRange = Duration.of(hours.toLong(), ChronoUnit.HOURS)
            timeRange = timeRange.plus(minutes.toLong(), ChronoUnit.MINUTES)
            var oldTime = intent.getSerializableExtra("oldTime") as ZonedDateTime
            var deadline = oldTime.plus(dateRange).plus(timeRange)

            val name = intent.getStringExtra("name")
            val icon = intent.getStringExtra("icon")
            val phoneNumber = intent.getStringExtra("phoneNumber")

            if (name != null) {
                ContactItem.packageIntent(data, icon, name, deadline, phoneNumber, oldTime, dateRange, timeRange)
            }
            // return data Intent and the finish
            setResult(RESULT_OK, data)
            finish()
        }
    }

    //object that is used
    companion object {
        private const val TAG = "CallYourMother-UpdateContactActivity"
    }
}

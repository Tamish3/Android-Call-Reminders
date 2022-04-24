package course.labs.todomanager

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
//import course.labs.todomanager.ToDoItem.Priority
//import course.labs.todomanager.ToDoItem.Status
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

    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_todo_new)

        val button = findViewById<Button>(R.id.contactsButton)
        button.setOnClickListener {
            pickContactIntent()
        }

        val dropdown = findViewById<Spinner>(R.id.spinner1)
        //create a list of items for the spinner.
        //create a list of items for the spinner.
        val items = arrayOf("24 hours", "1 week", "1 month", "6 months", "1 year", "Custom")
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        //set the spinners adapter to the previously created one.
        //set the spinners adapter to the previously created one.
        dropdown.adapter = adapter
    }

    fun pickContactIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PERMISSIONS_PICK_CONTACT_REQUEST)
    }

    private fun setDefaultDateTime() {

        // Default is current time + 7 days
        mDate = Date()
        mDate = Date(mDate.time + SEVEN_DAYS)

        val c = Calendar.getInstance()
        c.time = mDate

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH))

        dateView.text = dateString

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))

        timeView.text = timeString
    }

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
    }
}

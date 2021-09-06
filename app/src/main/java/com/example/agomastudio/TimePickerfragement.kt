package com.example.agomastudio

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*

class TimePickerfragement: DialogFragment(), TimePickerDialog.OnTimeSetListener{
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = calendar.get(Calendar.HOUR_OF_DAY).toInt()
        val minute = calendar.get(Calendar.MINUTE).toInt()
        return TimePickerDialog(requireActivity(), this, hour,minute,true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
        calendar.set(Calendar.MINUTE,minute)
        val selectedDate = SimpleDateFormat("HH:mm").format(calendar.time)
        val selectedDateBundle = Bundle()
        selectedDateBundle.putString("SELECTED_DATE",selectedDate)

        setFragmentResult("REQUEST_KEY",selectedDateBundle)
    }
}
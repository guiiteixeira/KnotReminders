package com.example.knot

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.knot.model.Frequency
import com.example.knot.model.Reminder
import com.example.knot.repository.ReminderRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ReminderActivity : AppCompatActivity() {

    lateinit var title: TextInputEditText
    lateinit var date: EditText
    lateinit var frequency: Spinner
    lateinit var description: EditText
    lateinit var resume: EditText
    var editar: Boolean = false
    lateinit var reminder: Reminder
    lateinit var reminderRePository: ReminderRepository
    lateinit var dateFormatted: String
    lateinit var timeFormatted: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        title = findViewById(R.id.title)
        date = findViewById(R.id.date)
        frequency = findViewById(R.id.spinner)
        description = findViewById(R.id.description)
        resume = findViewById(R.id.resume)

        var id = intent.extras?.getSerializable("reminder") as Long?
        this.reminderRePository = intent.extras?.getSerializable("repository") as ReminderRepository

        ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                frequency.adapter = adapter
            }

        if(id != null) {
            fillReminderToEdit(id)
            setFrequency()
        }

        date.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                onDateFocus(view)
            }
        })
    }

    fun setFrequency(){

        when(reminder.frequency){
            Frequency.NUNCA -> {
                frequency.setSelection(0)
                true
            }
            Frequency.UNICA_VEZ -> {
                frequency.setSelection(1)
                true
            }
            Frequency.HORA_EM_HORA -> {
                frequency.setSelection(2)
                true
            }
            Frequency.DIARIO -> {
                frequency.setSelection(3)
                true
            }
            Frequency.SEMANAL -> {
                frequency.setSelection(4)
                true
            }
            Frequency.MENSAL -> {
                frequency.setSelection(5)
                true
            }
        }
    }

    fun fillReminderToEdit(id: Long){
        editar = true
        this.reminder = reminderRePository.getById(id) as Reminder
        title.setText(reminder.title)

        var formater = SimpleDateFormat("dd/MM/yyyy HH:mm")
        date.setText(formater.format(reminder.firstAlertDate))

        description.setText(reminder.description)
        resume.setText(reminder.resume)

        title.isEnabled = false
        date.isEnabled = false
        frequency.isEnabled = false
        description.isEnabled = false
        resume.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(editar) {
            menuInflater.inflate(R.menu.menu_secondary_layout, menu)
        }
        return true
    }

    fun removerReminder(){
        if(editar){
            this.reminderRePository.deleteReminder(this.reminder)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.removeReminderBtn -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.confirmDeletion))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->

                    }
                    .setPositiveButton(resources.getString(R.string.confirm)) { dialog, which ->
                        removerReminder()
                    }
                    .show()
                true
            }
            R.id.editReminderBtn -> {
                title.isEnabled = true
                date.isEnabled = true
                frequency.isEnabled = true
                description.isEnabled = true
                resume.isEnabled = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onCancelClick(view: View){
        finish()
    }

    fun getReminder(){

        var resumeText = resume.text.toString()
        var titleText = title.text.toString()
        var descriptionText = description.text.toString()

        var frequency = frequency.selectedItem.toString()
        var frequencyParam: Frequency?

        val array = resources.getStringArray(R.array.spinner_options) as Array<String>

        when(frequency){
            array.get(0) -> {
                frequencyParam = Frequency.NUNCA
                true
            }
            array.get(1) -> {
                frequencyParam = Frequency.UNICA_VEZ
                true
            }
            array.get(2) -> {
                frequencyParam = Frequency.HORA_EM_HORA
                true
            }
            array.get(3) -> {
                frequencyParam = Frequency.DIARIO
                true
            }
            array.get(4) -> {
                frequencyParam = Frequency.SEMANAL
                true
            }
            array.get(5) -> {
                frequencyParam = Frequency.MENSAL
                true
            }
            else -> {
                frequencyParam = Frequency.UNICA_VEZ
                true
            }
        }

        var formater = SimpleDateFormat("dd/MM/yyyy HH:mm")
        lateinit var date: Date

        try {
            date = formater.parse(this.date.text.toString())
        }catch ( ex: ParseException){
            date = Date()
        }


        if(editar){
            reminder.title = titleText
            reminder.resume = resumeText
            reminder.description = descriptionText
            reminder.firstAlertDate = date
            reminder.frequency = frequencyParam
        }else {
            this.reminder = Reminder(
                0L,
                    titleText,
                    resumeText,
                    descriptionText,
                    Date(),
                    frequencyParam,
                    date
            )
        }
    }

    fun onSaveClick(view: View){
        getReminder()
        if(editar){
            this.reminderRePository.updateReminder(reminder)
        }else{
            this.reminderRePository.addReminder(reminder)
        }
        finish()
    }

    fun onDateFocus(view: View){

        lateinit var date: Date
        if(editar){
           date = this.reminder.firstAlertDate
        }else{
            date = Date()
        }

        var calendar = Calendar.getInstance()
        calendar.time = date

        val datePickerDialog = DatePickerDialog(
            this@ReminderActivity,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dateFormatted = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)

                val timePickerDialog = TimePickerDialog(
                    this@ReminderActivity,
                    OnTimeSetListener { view, hourOfDay, minute ->
                        timeFormatted = (hourOfDay.toString() + ":" + minute.toString())
                        this.date.setText(dateFormatted + " " + timeFormatted)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
        true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}

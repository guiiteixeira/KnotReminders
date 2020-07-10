package com.example.knot

import android.annotation.TargetApi
import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.JobIntentService.enqueueWork
import com.example.knot.model.Frequency
import com.example.knot.model.Reminder
import com.example.knot.repository.ReminderRepository
import com.example.knot.tasks.FillEditScreenTask
import com.example.knot.tasks.alarm.AlarmReceiver
import com.example.knot.tasks.alarm.NotificationIntentService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException


class ReminderActivity : AppCompatActivity() {

    lateinit var title: TextInputEditText
    lateinit var date: EditText
    lateinit var frequency: Spinner
    lateinit var description: EditText
    lateinit var resume: EditText
    var editar: Boolean = false
    var hadFrequency: Boolean = false
    lateinit var reminder: Reminder
    lateinit var reminderRePository: ReminderRepository
    lateinit var dateFormatted: String
    lateinit var timeFormatted: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        title = findViewById(R.id.title)
        date = findViewById(R.id.date)
        frequency = findViewById(R.id.spinner) as Spinner
        description = findViewById(R.id.description)
        resume = findViewById(R.id.resume)

        reminderRePository = ReminderRepository(this)
        var index = intent.extras?.getSerializable("reminder") as Int?


        ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                frequency.adapter = adapter
            }

        if(index != null) {
            fillReminderToEdit(index)
            Thread(Runnable {
                this.reminder = reminderRePository.getByIndex(index) as Reminder
                if(reminder.frequency != Frequency.NUNCA) hadFrequency = true
            }).start()
        }

        date.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                onDateFocus(view)
            }
        })


    }

    fun fillReminderToEdit(index: Int){
        editar = true

        FillEditScreenTask(reminderRePository,title,date,frequency,description,resume, index).execute()

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
            if(hadFrequency){
                cancelaAlarme()
            }
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
            if(hadFrequency){
                cancelaAlarme()
            }
            if(reminder.frequency != Frequency.NUNCA){
                criaAlarme()
            }
        }else{
            this.reminderRePository.addReminder(reminder)
            if(reminder.frequency != Frequency.NUNCA){
                criaAlarme()
            }
        }
        finish()
    }


    fun criaAlarme(){

        var bundle: Bundle = Bundle()
        bundle.putSerializable("reminder",reminder)

        var intent: Intent = Intent(applicationContext,NotificationIntentService::class.java).apply {
            putExtras(bundle)
        }

        startService(intent)

    }

    fun cancelaAlarme(){
        val alarmManager =
            getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        var notificationIntent = Intent(this, AlarmReceiver::class.java)

        var gson: Gson = Gson()

        notificationIntent.action = reminder.id.toString()
        notificationIntent.putExtra("reminder",gson.toJson(reminder))

        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext,1,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager?.cancel(pendingIntent)
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

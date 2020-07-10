package com.example.knot.tasks

import android.os.AsyncTask
import android.widget.EditText
import android.widget.Spinner
import com.example.knot.model.Frequency
import com.example.knot.model.Reminder
import com.example.knot.repository.ReminderRepository
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat

class FillEditScreenTask(
    repository: ReminderRepository,
    title: TextInputEditText,
    date: EditText,
    frequency: Spinner,
    description: EditText,
    resume: EditText,
    id: Int
    ) : AsyncTask<Void, Void, Reminder>() {

    var repository: ReminderRepository
    var title: TextInputEditText
    var date: EditText
    var frequency: Spinner
    var description: EditText
    var resume: EditText
    var id: Int

    init{
        this.repository = repository
        this.title = title
        this.date = date
        this.frequency = frequency
        this.description = description
        this.resume = resume
        this.id = id
    }

    override fun doInBackground(vararg params: Void?): Reminder {
        return repository.getByIndex(id) as Reminder
    }


    override fun onPostExecute(result: Reminder) {
        super.onPostExecute(result)

        title.setText(result.title)

        var formater = SimpleDateFormat("dd/MM/yyyy HH:mm")
        date.setText(formater.format(result.firstAlertDate))

        description.setText(result.description)
        resume.setText(result.resume)

        when(result.frequency){
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
}
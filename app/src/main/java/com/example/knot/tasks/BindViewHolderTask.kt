package com.example.knot.tasks

import android.os.AsyncTask
import com.example.knot.adapter.CardAdapter
import com.example.knot.model.Reminder
import com.example.knot.repository.ReminderRepository
import java.text.FieldPosition
import java.text.SimpleDateFormat

class BindViewHolderTask(repository: ReminderRepository, holder: CardAdapter.CardViewHolder,position: Int) : AsyncTask<Void, Void, Reminder>() {

    var repository: ReminderRepository
    var holder: CardAdapter.CardViewHolder
    var position: Int

    init{
        this.repository = repository
        this.holder = holder
        this.position = position
    }

    override fun doInBackground(vararg params: Void): Reminder {
        return repository.getByIndex(position) as Reminder
    }

    override fun onPostExecute(result: Reminder) {
        super.onPostExecute(result)

        holder.id = result.id

        holder.title.setText(result.title)
        var formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        holder.date.setText(formatter.format(result.firstAlertDate))
        holder.resume.setText(result.resume)
    }
}
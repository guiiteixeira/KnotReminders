package com.example.knot.repository

import android.content.Context
import android.os.AsyncTask
import com.example.knot.db.AppDatabase
import com.example.knot.db.Database
import com.example.knot.model.Reminder
import com.example.knot.tasks.DeleteReminderTask
import com.example.knot.tasks.InsertReminderTask
import com.example.knot.tasks.UpdateReminderTask
import java.io.Serializable
import kotlin.collections.ArrayList

class ReminderRepository(var context: Context): Serializable {

    val db: AppDatabase

    init {
        db = Database.getDatabase(context) as AppDatabase
    }

    fun addReminder(reminder: Reminder){
        InsertReminderTask(db,reminder).execute()
    }

    fun deleteReminder(reminder: Reminder){
        DeleteReminderTask(db,reminder).execute()
    }

    fun getById(reminderId: Long): Reminder?{
        return db.reminderDAO().getById(reminderId)
    }

    fun getByIndex(index: Int): Reminder?{
        var reminders: List<Reminder> = db.reminderDAO().getAll()
        return reminders.get(index)
    }

    fun getItemCount(): Int{
        var reminders: List<Reminder> = db.reminderDAO().getAll()
        return reminders.size
    }

    fun updateReminder(reminder: Reminder){
        UpdateReminderTask(db,reminder).execute()
    }
}
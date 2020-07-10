package com.example.knot.tasks

import android.os.AsyncTask
import com.example.knot.db.AppDatabase
import com.example.knot.model.Reminder

class DeleteReminderTask(db: AppDatabase, reminder: Reminder) : AsyncTask<Void, Void, Unit>() {

    var db: AppDatabase
    var reminder: Reminder

    init{
        this.db = db
        this.reminder = reminder
    }

    override fun doInBackground(vararg params: Void) {
        db.reminderDAO().delete(reminder)
    }

}
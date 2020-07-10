package com.example.knot.tasks.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.knot.model.Reminder
import com.google.gson.Gson

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        var gson: Gson = Gson()
        var reminder: Reminder? = gson.fromJson(intent?.getStringExtra("reminder"),Reminder::class.java)

        if(reminder != null){
            var notificationHelper = NotificationHelper(context as Context, reminder)
            var builder: NotificationCompat.Builder = notificationHelper.getNotification()
            notificationHelper.getManager().notify(1, builder.build())
        }


    }
}
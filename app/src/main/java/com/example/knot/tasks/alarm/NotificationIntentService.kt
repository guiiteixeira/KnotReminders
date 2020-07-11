package com.example.knot.tasks.alarm

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.knot.model.Frequency
import com.example.knot.model.Reminder
import com.google.gson.Gson
import java.util.*

class NotificationIntentService : IntentService("CreateNotification") {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onHandleIntent(intent: Intent?) {

        var reminder: Reminder = intent?.extras?.getSerializable("reminder") as Reminder

        var cal: Calendar = Calendar.getInstance()
        cal.time = reminder.firstAlertDate

        val alarmManager =
            getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        var notificationIntent = Intent(applicationContext, AlarmReceiver::class.java)

        var gson: Gson = Gson()

        notificationIntent.action = reminder.id.toString()
        notificationIntent.putExtra("reminder",gson.toJson(reminder))

        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext,1,notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        when(reminder.frequency){
            Frequency.UNICA_VEZ -> {
                alarmManager?.setExact(AlarmManager.RTC_WAKEUP,cal.timeInMillis,pendingIntent)
            }
            Frequency.HORA_EM_HORA -> {
                alarmManager?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    cal.timeInMillis,
                    AlarmManager.INTERVAL_HOUR,
                    pendingIntent
                )
            }
            Frequency.DIARIO -> {
                alarmManager?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    cal.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            Frequency.SEMANAL -> {
                alarmManager?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    cal.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
                )
            }
            Frequency.MENSAL -> {
                alarmManager?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    cal.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 30,
                    pendingIntent
                )
            }
        }
    }
}
package com.example.knot.tasks.alarm

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.knot.R
import com.example.knot.model.Reminder

class NotificationHelper(base: Context, reminder: Reminder) : ContextWrapper(base) {
    var notificationManager: NotificationManager? = null
    var reminder: Reminder

    init{
        this.reminder = reminder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel(){
        var notificationChannel: NotificationChannel = NotificationChannel(reminder.id.toString(), reminder.title, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.lightColor = R.color.colorPrimary
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        getManager().createNotificationChannel(notificationChannel)
    }

    fun getManager(): NotificationManager {
        if(notificationManager == null){
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        return notificationManager as NotificationManager
    }

    fun getNotification(): NotificationCompat.Builder{
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return NotificationCompat.Builder(applicationContext,reminder.id.toString())
                .setContentTitle(reminder.title)
                .setContentText(reminder.resume)
                .setSmallIcon(R.drawable.knotfinger_small_transparent)
                .setColor(resources.getColor(R.color.colorPrimary))
        } else {
            return NotificationCompat.Builder(applicationContext,reminder.id.toString())
                .setContentTitle(reminder.title)
                .setContentText(reminder.resume)
                .setSmallIcon(R.drawable.knotfinger_small_round)
        }

    }

}
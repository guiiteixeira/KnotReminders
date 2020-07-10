package com.example.knot.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.knot.converters.Converters
import com.example.knot.model.Reminder

@Database(entities = arrayOf(Reminder::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDAO(): ReminderDAO
}
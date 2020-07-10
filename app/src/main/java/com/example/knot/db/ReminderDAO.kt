package com.example.knot.db

import androidx.room.*
import com.example.knot.model.Reminder

@Dao
interface ReminderDAO {
    @Query("SELECT * FROM reminder")
    fun getAll(): List<Reminder>

    @Query("SELECT * FROM reminder WHERE id IN (:reminderIds)")
    fun loadAllByIds(reminderIds: LongArray): List<Reminder>

    @Query("SELECT * FROM reminder WHERE id = (:reminderId)")
    fun getById(reminderId: Long): Reminder

    @Insert
    fun insertAll(vararg reminders: Reminder)

    @Update
    fun updateReminder(reminder: Reminder)

    @Delete
    fun delete(reminder: Reminder)
}
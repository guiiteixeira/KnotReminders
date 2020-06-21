package com.example.knot.repository

import com.example.knot.model.Frequency
import com.example.knot.model.Reminder
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class ReminderRepository: Serializable {

    companion object{
        var reminders: ArrayList<Reminder> = ArrayList()
        var index: Long = 2
    }

    constructor(){
        reminders.add(Reminder(
            1,
            "Example",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been " +
                    "the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of " +
                    "type and scrambled it to make a type specimen book. ",
            Date(),
            Frequency.HORA_EM_HORA,
            Date()
        ))
    }

    fun addReminder(reminder: Reminder){
        reminder.id = index
        index += 1
        reminders.add(reminder)
    }

    fun deleteReminder(reminder: Reminder){
        reminders.remove(reminder)
    }

    fun listAll(): ArrayList<Reminder> {
        return reminders
    }

    fun getById(reminderId: Long): Reminder?{
        for(item: Reminder in reminders){
            if(reminderId == item.id){
                return item
            }
        }
        return null
    }

    fun getByIndex(index: Int): Reminder?{
        if(reminders.size <= index || 0 > index){
            return null;
        }else{
            return reminders.get(index)
        }
    }

    fun getItemCount(): Int{
        return reminders.size
    }

    fun updateReminder(reminder: Reminder){
        for((index,value) in reminders.withIndex()){
            if(value.id == reminder.id){
                reminders.set(index,reminder)
                return
            }
        }
    }
}
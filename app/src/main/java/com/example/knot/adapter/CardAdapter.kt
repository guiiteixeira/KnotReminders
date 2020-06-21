package com.example.knot.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.knot.R
import com.example.knot.ReminderActivity
import com.example.knot.model.Reminder
import com.example.knot.repository.ReminderRepository
import java.text.SimpleDateFormat

class CardAdapter(reminderRepository: ReminderRepository, onReminderListener: CardViewHolder.OnReminderListener): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    var reminderRepository: ReminderRepository
    var onReminderListener: CardViewHolder.OnReminderListener


    init{
        this.reminderRepository = reminderRepository
        this.onReminderListener = onReminderListener
    }

    class CardViewHolder(view: View, onReminderListener: OnReminderListener): RecyclerView.ViewHolder(view), View.OnClickListener {

        var id: Long = 0
        var title: TextView
        var date: TextView
        var resume: TextView
        var onReminderListener: OnReminderListener

        init{
            title = view.findViewById(R.id.titleView)
            date = view.findViewById(R.id.alertDate)
            resume = view.findViewById(R.id.resume)

            this.onReminderListener = onReminderListener

            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onReminderListener.onReminderClick(adapterPosition)
        }

        interface OnReminderListener{
            fun onReminderClick(position: Int)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_layout,parent,false) as LinearLayout
        var viewHolder = CardViewHolder(view, this.onReminderListener)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return this.reminderRepository.getItemCount()
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        var reminder: Reminder = reminderRepository.getByIndex(position) as Reminder

        holder.id = reminder.id

        holder.title.setText(reminder.title)
        var formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        holder.date.setText(formatter.format(reminder.firstAlertDate))
        holder.resume.setText(reminder.resume)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }
}
package com.example.cautiondoyouremember.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.reminders.Reminder
import com.google.android.gms.vision.text.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReminderAdapter(private var allReminderFromDatabase:ArrayList<Reminder>):
    RecyclerView.Adapter<ReminderAdapter.ReminderAdapterRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderAdapter.ReminderAdapterRvViewHolder {
        return ReminderAdapterRvViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.reminder_item_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReminderAdapter.ReminderAdapterRvViewHolder, position: Int) {
        val currentReminder = allReminderFromDatabase[position]
        holder.reminderDesc.text = currentReminder.ReminderDescription.toString()
        val dateLong: Long? = currentReminder.ReminderTime?.toLong()
        val date = dateLong?.let { Date(it) }
        val format =  SimpleDateFormat("dd/MM/yyyy @ hh:mm a")
        val reminderDate = format.format(date)
        holder.reminderTime.text = reminderDate
    }

    override fun getItemCount(): Int {
        return allReminderFromDatabase.size
    }

    class ReminderAdapterRvViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val reminderDesc: TextView =view.findViewById(R.id.ReminderDescription)
        val reminderTime: TextView = view.findViewById(R.id.ReminderTime)
    }
}
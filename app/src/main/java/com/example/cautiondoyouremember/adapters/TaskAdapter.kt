package com.example.cautiondoyouremember.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.tasks.Task
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TaskAdapter(private var allTasksFromDatabase:ArrayList<Task>)
    :RecyclerView.Adapter<TaskAdapter.TasksRvAdapterViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksRvAdapterViewHolder {
        return TasksRvAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TasksRvAdapterViewHolder, position: Int) {
        val currentTask = allTasksFromDatabase[position]
        holder.taskTitle.text = currentTask.TaskTitle
        holder.taskDesc.text = currentTask.TaskDescription

        val datelong: Long? = currentTask.TaskTime?.toLong()
        val date = currentTask.TaskTime?.let { Date(it) }
        val format = SimpleDateFormat("dd/MM/yyyy @ hh:mm a")
        val taskDate = date?.let { format.format(it) }
        holder.taskDate.text = taskDate
        if (!currentTask.Status) {
            holder.taskStatus.text = "Pending"
        }
    }

    override fun getItemCount(): Int {
        return allTasksFromDatabase.size
    }

    fun updateNotesList(updatedList:List<Task>) {
        allTasksFromDatabase.clear()
        allTasksFromDatabase = (updatedList as ArrayList<Task>).clone() as ArrayList<Task>
        notifyDataSetChanged()
    }


    class TasksRvAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val taskTitle: TextView = view.findViewById(R.id.TaskName)
        val taskDesc: TextView = view.findViewById(R.id.TaskDesc)
        val taskDate: TextView = view.findViewById(R.id.TaskDate)
        val taskStatus: TextView = view.findViewById(R.id.TaskStatus)
    }

}
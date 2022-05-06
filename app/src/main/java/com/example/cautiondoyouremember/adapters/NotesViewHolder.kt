package com.example.cautiondoyouremember.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cautiondoyouremember.R

class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val noteTitle: TextView = view.findViewById(R.id.noteTitle)
    val noteDescription: TextView = view.findViewById(R.id.noteDesc)
    val noteDate: TextView = view.findViewById(R.id.noteDate)
}
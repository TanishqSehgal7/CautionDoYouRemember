package com.example.cautiondoyouremember.adapters

import android.app.DownloadManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.notes.Note
import com.google.firebase.database.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(private var allNotesFromDatabase:ArrayList<Note>
): RecyclerView.Adapter<NotesAdapter.NotesRvAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesRvAdapterViewHolder {
        val viewHolder = NotesRvAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item_card,parent,false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: NotesRvAdapterViewHolder, position: Int) {
        val currentNote = allNotesFromDatabase[position]
        holder.noteTitle.text = currentNote.noteTitle
        holder.noteDescription.text = currentNote.noteDesc
        holder.noteDate.text = currentNote.time.toString()
    }

    fun updateNotesList(updatedList:List<Note>) {
        allNotesFromDatabase.clear()
        allNotesFromDatabase = (updatedList as ArrayList<Note>).clone() as ArrayList<Note>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return allNotesFromDatabase.size
    }

    inner class NotesRvAdapterViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val noteTitle:TextView = view.findViewById(R.id.noteTitle)
        val noteDescription: TextView = view.findViewById(R.id.noteDesc)
        val noteDate: TextView = view.findViewById(R.id.noteDate)
    }
}
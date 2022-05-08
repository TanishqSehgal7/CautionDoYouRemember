package com.example.cautiondoyouremember.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.notes.Note
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(private var allNotesFromDatabase:ArrayList<Note>
): RecyclerView.Adapter<NotesAdapter.NotesRvAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesRvAdapterViewHolder {
        return NotesRvAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotesRvAdapterViewHolder, position: Int) {
        val currentNote = allNotesFromDatabase[position]
        holder.noteTitle.text = currentNote.NoteTitle
        holder.noteDescription.text = currentNote.NoteDescription

//        val date = currentNote.time?.let { Date(it.toLong()) }
//        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        val noteDate = format.format(date)

        holder.noteDate.text = currentNote.NoteDate
    }

    fun updateNotesList(updatedList:List<Note>) {
        allNotesFromDatabase.clear()
        allNotesFromDatabase = (updatedList as ArrayList<Note>).clone() as ArrayList<Note>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return allNotesFromDatabase.size
    }

    class NotesRvAdapterViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val noteTitle:TextView = view.findViewById(R.id.noteTitle)
        val noteDescription: TextView = view.findViewById(R.id.noteDesc)
        val noteDate: TextView = view.findViewById(R.id.noteDate)
    }
}
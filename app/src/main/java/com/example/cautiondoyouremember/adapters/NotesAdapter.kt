package com.example.cautiondoyouremember.adapters

import android.util.Log
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

        val datelong: Long? = currentNote.NoteDate?.toLong()
        if (datelong != null) {
            Log.d("LongValue", datelong.javaClass.simpleName)
        }
        val date = datelong?.let { Date(it) }
        val format = SimpleDateFormat("dd/MM/yyyy @ hh:mm a")
        val noteDate = date?.let { format.format(it) }
        holder.noteDate.text = noteDate
    }

    override fun getItemCount(): Int {
        return allNotesFromDatabase.size
    }

    fun updateNotesList(updatedList:List<Note>) {
        allNotesFromDatabase.clear()
        allNotesFromDatabase = (updatedList as ArrayList<Note>).clone() as ArrayList<Note>
        notifyDataSetChanged()
    }

    class NotesRvAdapterViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val noteTitle:TextView = view.findViewById(R.id.noteTitle)
        val noteDescription: TextView = view.findViewById(R.id.noteDesc)
        val noteDate: TextView = view.findViewById(R.id.noteDate)
    }
}
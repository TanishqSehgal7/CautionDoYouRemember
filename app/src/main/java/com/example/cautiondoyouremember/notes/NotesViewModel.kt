package com.example.cautiondoyouremember.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotesViewModel (val googleId: String,private var notesRepository:NoteRepository = NoteRepository(googleId))
    : ViewModel() {

    val allNotesLiveData: MutableLiveData<NotesResponse>

    init {
        notesRepository = NoteRepository(googleId)
        allNotesLiveData = notesRepository.allNotes
    }

    fun insertNewNote(note:Note, id:String) {
        notesRepository.insertNewNote(note,id)
    }

    fun deleteNote(note: Note, id:String) {
        notesRepository.deleteNote(note,id)
    }

    fun updateNote(note: Note, id:String) {
        notesRepository.updateNote(note,id)
    }

    fun noteResponseFromFirebaseAsMutableLiveData(): LiveData<NotesResponse> {
      return  notesRepository.noteResponseFromFirebaseAsMutableLiveData()
    }

}
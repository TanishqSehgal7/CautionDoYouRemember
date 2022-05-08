package com.example.cautiondoyouremember.notes

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

class NotesViewModel (application: Application) : AndroidViewModel(application) {

    val googleId = GoogleSignIn.getLastSignedInAccount(application)
    val notesRepository = NoteRepository(googleId?.id.toString())

    val allNotesLiveData: LiveData<List<Note>> = notesRepository.allNotes

    init {
        Log.d("GoogleIdInVM", googleId.toString())
        notesRepository.getListOfNotes()
    }

    fun insertNewNote(note:Note, id:String) {
        notesRepository.insertNewNote(note,id)
    }

    fun deleteNote(note: Note, id:String) {
        notesRepository.deleteNote(note,id)
    }
}
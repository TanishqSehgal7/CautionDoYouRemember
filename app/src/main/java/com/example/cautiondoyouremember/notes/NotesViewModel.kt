package com.example.cautiondoyouremember.notes

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

class NotesViewModel (
                      application: Application
)
    : AndroidViewModel(application) {

    val googleId = GoogleSignIn.getLastSignedInAccount(application)
    val notesRepository = NoteRepository(googleId.toString())

    val allNotesLiveData: LiveData<List<Note>> = notesRepository.getNotes()

    init {
        Log.d("GoogleIdInVM", googleId.toString())
    }

    fun insertNewNote(note:Note, id:String) {
        notesRepository.insertNewNote(note,id)
    }

    fun deleteNote(note: Note, id:String) {
        notesRepository.deleteNote(note,id)
    }

//    fun updateNote(note: Note, id:String) {
//        notesRepository.updateNote(note,id)
//    }

    fun noteResponseFromFirebaseAsLiveData(): LiveData<List<Note>> {
      return  notesRepository.getNotes()
    }

}
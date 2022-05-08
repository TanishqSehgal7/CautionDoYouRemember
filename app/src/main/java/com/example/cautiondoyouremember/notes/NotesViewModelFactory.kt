package com.example.cautiondoyouremember.notes

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn

//class NotesViewModelFactory(private val noteRepository: NoteRepository, val context: Context)
//    : ViewModelProvider.Factory {
//    val acct = GoogleSignIn.getLastSignedInAccount(context)
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return NotesViewModel() as T
//    }
//
//}
package com.example.cautiondoyouremember.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.cautiondoyouremember.databinding.ActivityAddNoteBinding
import com.example.cautiondoyouremember.notes.Note
import com.example.cautiondoyouremember.notes.NoteRepository
import com.example.cautiondoyouremember.notes.NotesViewModel
import com.example.cautiondoyouremember.notes.NotesViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddNoteBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var noteID:String
    private lateinit var waitingDialog : android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteID = UUID.randomUUID().toString()

        binding.backbtnNoteAct.setOnClickListener {
            finish()
        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val notesRepository = NoteRepository(acct?.id.toString())

        //initialize viewModel
        viewModel = ViewModelProvider(this, NotesViewModelFactory(notesRepository,this))
            .get(NotesViewModel::class.java)

        binding.saveNote.setOnClickListener {

            val titleOfNote = binding.NoteTitle.text.toString()
            val descOfNote = binding.NoteDesc.text.toString()
            val timeOfNote = System.currentTimeMillis()

            val note = Note(noteID)
            note.noteTitle = titleOfNote
            note.noteDesc = descOfNote
            note.date = timeOfNote

            if (acct != null) {
                viewModel.insertNewNote(note, noteID)
                waitingDialog = SpotsDialog.Builder().setContext(this)
                    .setMessage("Saving Note...")
                    .setCancelable(true)
                    .build().apply {
                        show()
                        Handler().postDelayed(object : Runnable {
                            override fun run() {
                                waitingDialog.dismiss()
                                finish()
                            }
                        }, 2000)
                    }
            }
        }
    }
}
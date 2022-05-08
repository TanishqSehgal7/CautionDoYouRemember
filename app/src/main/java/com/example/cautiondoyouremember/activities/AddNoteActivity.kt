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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

        val dateLong = System.currentTimeMillis()
        val date = dateLong.let { Date(it) }
        val format =  SimpleDateFormat("dd/MM/yyyy @ hh:mm a")
        val reminderDate = format.format(date)

        binding.CurrentDate.text = reminderDate

        //initialize viewModel
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        // save notes to firebase realtime database
        binding.saveNote.setOnClickListener {

            if (binding.NoteTitle.text?.isEmpty() == true || binding.NoteDesc.text.isEmpty()) {
                binding.NoteTitle.requestFocus()
                binding.NoteDesc.requestFocus()
                binding.NoteTitle.error = "Note Title can't be empty!"
                binding.NoteDesc.error = "Note Description can't be empty!"
            } else {

                val titleOfNote = binding.NoteTitle.text.toString()
                val descOfNote = binding.NoteDesc.text.toString()
                val timeOfNote = System.currentTimeMillis().toString()

                val note = Note(titleOfNote, descOfNote, timeOfNote)

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
}
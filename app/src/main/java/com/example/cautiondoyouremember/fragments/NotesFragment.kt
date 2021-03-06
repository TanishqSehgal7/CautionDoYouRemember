package com.example.cautiondoyouremember.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.activities.MainActivity
import com.example.cautiondoyouremember.adapters.NotesAdapter
import com.example.cautiondoyouremember.notes.Note
import com.example.cautiondoyouremember.notes.NoteRepository
import com.example.cautiondoyouremember.notes.NotesViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.*

class NotesFragment : Fragment() {

    lateinit var notesRecyclerView: RecyclerView
    lateinit var notesAdapter: NotesAdapter
    private lateinit var viewModel: NotesViewModel
    private lateinit var allNotes:ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val acct = GoogleSignIn.getLastSignedInAccount(this.requireContext())
        val rootReferenceForNotes: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val noteReference: DatabaseReference = rootReferenceForNotes.child(acct?.id.toString()).child("Notes")

        val view:View = inflater.inflate(R.layout.fragment_notes, container, false)
        val notesRepository = NoteRepository(acct?.id.toString())

        notesRecyclerView = view.findViewById(R.id.notesRv)
//        viewModel = ViewModelProvider(this, NotesViewModelFactory(notesRepository, this.requireContext())).get(NotesViewModel::class.java)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        allNotes = ArrayList<Note>()

        viewModel.allNotesLiveData.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "$it[0]", Toast.LENGTH_SHORT).show()
            notesRecyclerView.setHasFixedSize(true)
            notesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            allNotes = it as ArrayList<Note>
            notesAdapter = NotesAdapter(allNotes)
            notesRecyclerView.adapter = notesAdapter
            Log.d("AllNotes", it.toString())
        }
//        noteReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("SnapshotString", snapshot.value.toString())
//                if (snapshot.exists()) {
//                    for (noteSnapShot in snapshot.children) {
//                        Log.d("Notes", noteSnapShot.toString())
//                        val noteId = noteSnapShot.key
//                        val noteTitle = noteSnapShot.child("NoteTitle").value
//                        val noteDescription = noteSnapShot.child("NoteDescription").value
//                        val noteTime = noteSnapShot.child("NoteDate").value
//                        val note = Note(noteTitle.toString(),noteDescription.toString(), noteTime.toString())
//                        allNotes.add(note)
//                    }
//                    notesAdapter.updateNotesList(allNotes)
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//
//        notesAdapter = NotesAdapter(allNotes)
//        notesRecyclerView.adapter = notesAdapter
//        notesAdapter.onAttachedToRecyclerView(notesRecyclerView)
//        getAllNotesFromDb()
        return view
    }
}
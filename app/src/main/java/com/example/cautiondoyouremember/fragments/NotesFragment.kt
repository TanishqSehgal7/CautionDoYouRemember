package com.example.cautiondoyouremember.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.adapters.NotesAdapter
import com.example.cautiondoyouremember.adapters.NotesViewHolder
import com.example.cautiondoyouremember.notes.Note
import com.example.cautiondoyouremember.notes.NoteRepository
import com.example.cautiondoyouremember.notes.NotesViewModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.*

class NotesFragment : Fragment() {

    private val allNotesFromDatabase=ArrayList<Note>()
    lateinit var notesRecyclerView: RecyclerView
    lateinit var notesAdapter: NotesAdapter
//    var adapter: FirebaseRecyclerAdapter<Note, NotesViewHolder>? = null
    private lateinit var viewModel: NotesViewModel
    private lateinit var allNotes:ArrayList<Note>
    private val acct = activity?.let { GoogleSignIn.getLastSignedInAccount(it.applicationContext) }
    private val rootReferenceForNotes: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val noteReference: DatabaseReference = rootReferenceForNotes.child(acct?.id.toString()).child("Notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view:View = inflater.inflate(R.layout.fragment_notes, container, false)
        val notesRepository = NoteRepository(acct?.id.toString())

        notesRecyclerView = view.findViewById(R.id.notesRv)
        notesRecyclerView.setHasFixedSize(true)

        allNotes = arrayListOf<Note>()

//        val options = FirebaseRecyclerOptions.Builder<Note>().setQuery(noteReference, object :SnapshotParser<Note> {
//            override fun parseSnapshot(snapshot: DataSnapshot): Note {
//                return Note()
//            }
//        }).build()

//        Log.d("Options"," data : "+options.snapshots.forEach { note ->
//            note.id.toString() + " " + note.noteTitle.toString() + " " + note.noteDesc.toString()
//        });

//        adapter = object : FirebaseRecyclerAdapter<Note, NotesViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
//                val viewHolder = NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item_card,parent,false))
//                return viewHolder
//            }
//
//            override fun onBindViewHolder(holder: NotesViewHolder, position: Int, note: Note) {
////                val currentNote = allNotesFromDatabase[position]
//                holder.noteTitle.text = note.noteTitle
//                holder.noteDescription.text = note.noteDesc
//                holder.noteDate.text = note.time
//                Log.d("NoteTitle", note.noteTitle.toString())
//            }
//        }
//        adapter?.startListening();
//        val gridLayoutInflater= GridLayoutManager(activity?.applicationContext,2)
//        notesRecyclerView.layoutManager = gridLayoutInflater
//        notesRecyclerView.adapter = adapter

        noteReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("SnapshotString", snapshot.child("3602ff95-24c2-4488-a88a-330e9d97f0f8").child("NoteTitle").value.toString())
                if (snapshot.exists() &&  snapshot.hasChildren()) {
                    for (noteSnapShot in snapshot.children) {
                        val note: Note = noteSnapShot.getValue(Note::class.java)!!
                        allNotes.add(note)
                        Log.d("Notes", allNotes.toString())
                    }
                    notesAdapter = NotesAdapter(allNotes)
                    notesRecyclerView.adapter = notesAdapter
                    notesAdapter.notifyDataSetChanged()
//                    notesAdapter.updateNotesList(allNotes)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
//        getAllNotesFromDb()
        return view
    }

//    private fun getAllNotesFromDb() {
//        noteReference.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                allNotes.clear()
//                if (snapshot.exists() &&  snapshot.hasChildren()) {
//                    for (noteSnapShot in snapshot.children) {
//                        val note: Note? = noteSnapShot.getValue(Note::class.java)
//                        Log.d("SingleNote", note.toString())
//                        allNotes.add(note!!)
//                        Log.d("Notes", allNotes.toString())
//                    }
//                    notesAdapter = NotesAdapter(allNotes)
//                    notesRecyclerView.adapter = notesAdapter
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//    }

//    override fun onStart() {
//        super.onStart()
//        adapter?.startListening()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        adapter?.stopListening()
//    }

}
package com.example.cautiondoyouremember.notes
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class NoteRepository (private val googleId: String) {

    private val rootReferenceForNotes: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val noteReference: DatabaseReference = rootReferenceForNotes.child(googleId).child("Notes")

    var allNotes = MutableLiveData<List<Note>>()

    fun insertNewNote(note:Note, id:String) {
//        note.id=id
        noteReference.child(id).child("NoteTitle").setValue(note.NoteTitle)
        noteReference.child(id).child("NoteDescription").setValue(note.NoteDescription)
        noteReference.child(id).child("NoteDate").setValue(note.NoteDate)
    }

    fun deleteNote(note: Note, id:String) {
        noteReference.child(id).removeValue()
    }

//    fun updateNote(note: Note, id:String) {
//        note.id = id
//        val noteUpdated = Note(note.id)
//        val postValues = noteUpdated.toMap()
//        noteReference.child(note.id).updateChildren(postValues)
////        insertNewNote(note,id)
//    }

    fun getListOfNotes(){
        val list = arrayListOf<Note>()
        noteReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                Log.d("Repo","${snapshot.childrenCount}")
                for (noteItems in snapshot.children) {
                    val noteItem = noteItems.getValue(Note::class.java)
                    noteItem?.let { list.add(it) }
                }
                allNotes?.postValue(list)
                Log.d("NoteList", list.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("NoteList", error.toString())
            }
        })
    }


    fun noteResponseFromFirebaseAsMutableLiveData(): MutableLiveData<NotesResponse> {
        val mutableLiveDataForNotes = MutableLiveData<NotesResponse>()
        noteReference.get().addOnCompleteListener { task->
            val response = NotesResponse()
            if (task.isSuccessful) {
                val result = task.result
                result.let {
                    response.notes = result.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Note::class.java)!!
                    }
                }
            } else {
                response.exception =task.exception
            }
            mutableLiveDataForNotes.value = response
        }
        return mutableLiveDataForNotes
    }

    fun getResponseFromRealtimeDatabaseUsingCoroutines(): NotesResponse {
        val response = NotesResponse()
        try {
            response.notes = noteReference.get().result.children.map { snapShot ->
                snapShot.getValue(Note::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }

//    fun getAllNotesFromDb(): ArrayList<Note> {
//        val allNotes=ArrayList<Note>()
//        noteReference.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                allNotes.clear()
//                if (snapshot.exists() &&  snapshot.hasChildren()) {
//                    for (noteSnapShot in snapshot.children) {
//                        val note: Note? = noteSnapShot.getValue(Note::class.java)
//                        Log.d("SingleNote", note.toString())
//                        allNotes.add(note!!)
//                    }
//                    Log.d("SingleNote", allNotes.toString())
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//        return  allNotes
//    }
}
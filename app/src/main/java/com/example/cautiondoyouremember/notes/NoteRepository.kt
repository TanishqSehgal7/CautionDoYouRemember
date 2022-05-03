package com.example.cautiondoyouremember.notes
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NoteRepository (private val googleId: String) {

    private val rootReferenceForNotes: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val noteReference: DatabaseReference = rootReferenceForNotes.child(googleId).child("Notes")

    val allNotes: MutableLiveData<NotesResponse> = noteResponseFromFirebaseAsMutableLiveData()

    fun insertNewNote(note:Note, id:String) {
        note.id=id
        noteReference.child(note.id).child("NoteTitle").setValue(note.noteTitle)
        noteReference.child(note.id).child("NoteDescription").setValue(note.noteDesc)
        noteReference.child(note.id).child("NoteDate").setValue(note.date)
    }

    fun deleteNote(note: Note, id:String) {
        noteReference.child(note.id).removeValue()
    }

    fun updateNote(note: Note, id:String) {
        note.id = id
        val noteUpdated = Note(note.id)
        val postValues = noteUpdated.toMap()
        noteReference.child(note.id).updateChildren(postValues)
//        insertNewNote(note,id)
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
}
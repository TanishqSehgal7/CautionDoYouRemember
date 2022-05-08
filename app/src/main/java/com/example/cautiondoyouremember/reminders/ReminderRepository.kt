package com.example.cautiondoyouremember.reminders

import android.renderscript.Sampler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cautiondoyouremember.notes.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlin.math.absoluteValue

class ReminderRepository(private val googleId:String) {

    private val rootReferenceForReminders:DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val reminderReference: DatabaseReference = rootReferenceForReminders.child(googleId).child("Reminders")
    private val faceRecognitionDataReference: DatabaseReference = rootReferenceForReminders.child(googleId).child("FaceRecognitionRecords")

    val allReminders = MutableLiveData<List<Reminder>>()
    val allFaceRecogRecords = MutableLiveData<List<FaceRecoognitionData>>()

    fun insertNewReminder(reminder:Reminder,id:String) {
        reminderReference.child(id).child("ReminderDescription").setValue(reminder.ReminderDescription)
        reminderReference.child(id).child("ReminderTime").setValue(reminder.ReminderTime)
        reminderReference.child(id).child("isReminderCompletedStatus").setValue(reminder.isreminderCompletedStatus)
    }

    fun deleteReminder(reminder: Reminder, id:String) {
        reminderReference.child(id).removeValue()
    }

    fun getListOfReminders() {
        val listReminder = arrayListOf<Reminder>()
        reminderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listReminder.clear()
                Log.d("Repo", "${snapshot.childrenCount}")
                for (reminderItems in snapshot.children) {
                    val reminderItem = reminderItems.getValue(Reminder::class.java)
                    reminderItem?.let { listReminder.add(it) }
                }
                listReminder.sortByDescending { it.ReminderTime?.toLong()?.absoluteValue }
                allReminders.postValue(listReminder)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getFaceRecognitionRecords() {
        val listFaceRecogRecords = arrayListOf<FaceRecoognitionData>()
        faceRecognitionDataReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FaceRecognitionData", "$snapshot")
                for (faceRecogItems in snapshot.children) {
                    val faceRecorgRecord = faceRecogItems.getValue(FaceRecoognitionData::class.java)
                    faceRecorgRecord?.let { listFaceRecogRecords.add(it) }
                }
                listFaceRecogRecords.sortByDescending { it.whenFaceGotRecognized.toLong().absoluteValue }
                allFaceRecogRecords.postValue(listFaceRecogRecords)
                Log.d("FaceRecognitionData", allFaceRecogRecords.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


   fun reminderResponseFromFirebaseAsMutableLiveData(): MutableLiveData<ReminderResponse> {

        val mutableLiveDataForNotes = MutableLiveData<ReminderResponse>()
        reminderReference.get().addOnCompleteListener { reminder->
            val response = ReminderResponse()
            if (reminder.isSuccessful) {
                val result = reminder.result
                result.let {
                    response.reminders = result.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Reminder::class.java)!!
                    }
                }
            } else {
                response.exception =reminder.exception
            }
            mutableLiveDataForNotes.value = response
        }
        return mutableLiveDataForNotes
    }
}

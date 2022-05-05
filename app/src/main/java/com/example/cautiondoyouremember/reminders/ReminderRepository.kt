package com.example.cautiondoyouremember.reminders

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ReminderRepository(private val googleId:String) {

    private val rootReferenceForReminders:DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val reminderReference: DatabaseReference = rootReferenceForReminders.child(googleId).child("Reminders")
    private val faceRecognitionDataReference: DatabaseReference = rootReferenceForReminders.child(googleId).child("FaceRecognitionData")

    val allReminders: MutableLiveData<ReminderResponse> = reminderResponseFromFirebaseAsMutableLiveData()

    fun insertNewReminder(reminder:Reminder,id:String) {
        reminder.id = id
        reminderReference.child(reminder.id).child("ReminderDescription").setValue(reminder.reminderDescription)
        reminderReference.child(reminder.id).child("ReminderTime").setValue(reminder.reminderTime)
        reminderReference.child(reminder.id).child("isReminderCompletedStatus").setValue(reminder.isreminderCompletedStatus)
    }

//    fun sendFaceRecognitionDataToFirebase(reminder: Reminder) {
//        reminderReference.child("FaceRecognitionTimeStamps").child(reminder.whenFaceRecognized.toString()).child()
//    }

    fun deleteReminder(reminder: Reminder, id:String) {
        reminderReference.child(reminder.id).removeValue()
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

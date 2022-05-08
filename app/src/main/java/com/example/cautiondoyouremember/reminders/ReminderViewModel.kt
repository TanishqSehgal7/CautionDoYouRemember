package com.example.cautiondoyouremember.reminders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.Dispatchers

class ReminderViewModel(application: Application) : AndroidViewModel(application){


    val googleId = GoogleSignIn.getLastSignedInAccount(application)
    val reminderRepository = ReminderRepository(googleId?.id.toString())
    val allReminderLiveData : LiveData<List<Reminder>> = reminderRepository.allReminders
    val allFaceRecognitionRecords : LiveData<List<FaceRecoognitionData>> = reminderRepository.allFaceRecogRecords

     init {
         reminderRepository.getListOfReminders()
         reminderRepository.getFaceRecognitionRecords()
     }

    fun insertNewReminder(reminder: Reminder, id:String) {
        reminderRepository.insertNewReminder(reminder,id)
    }

    fun deleteReminder(reminder: Reminder,id:String) {
        reminderRepository.deleteReminder(reminder,id)
    }

}
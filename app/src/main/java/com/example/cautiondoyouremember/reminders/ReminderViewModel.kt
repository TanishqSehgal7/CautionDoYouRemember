package com.example.cautiondoyouremember.reminders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers

class ReminderViewModel(val googleId:String,
private var reminderRepository: ReminderRepository = ReminderRepository(googleId))
    : ViewModel(){

     val allReminderLiveData: MutableLiveData<ReminderResponse>

     init {
         reminderRepository = ReminderRepository(googleId)
         allReminderLiveData = reminderRepository.allReminders
     }

    fun insertNewReminder(reminder: Reminder, id:String) {
        reminderRepository.insertNewReminder(reminder,id)
    }

    fun deleteReminder(reminder: Reminder,id:String) {
        reminderRepository.deleteReminder(reminder,id)
    }

    fun reminderResponseFirebaseasMutableLiveData(): LiveData<ReminderResponse> {
        return reminderRepository.reminderResponseFromFirebaseAsMutableLiveData()
    }

}
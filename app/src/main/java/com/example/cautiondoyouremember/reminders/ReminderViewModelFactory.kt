package com.example.cautiondoyouremember.reminders

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn

class ReminderViewModelFactory(private val reminderRepository: ReminderRepository, val context: Context)
    :ViewModelProvider.Factory{
    val acct = GoogleSignIn.getLastSignedInAccount(context)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReminderViewModel(acct?.id.toString(), reminderRepository) as T
    }


}
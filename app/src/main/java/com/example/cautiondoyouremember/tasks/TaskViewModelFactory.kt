package com.example.cautiondoyouremember.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn

class TaskViewModelFactory(private val taskRepository: TaskRepository, val context:Context)
    : ViewModelProvider.Factory{
    val acct = GoogleSignIn.getLastSignedInAccount(context)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskViewModel(acct?.id.toString(),taskRepository) as T
    }

}
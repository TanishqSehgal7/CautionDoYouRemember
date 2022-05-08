package com.example.cautiondoyouremember.tasks

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cautiondoyouremember.notes.Note
import com.example.cautiondoyouremember.notes.NoteRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn

class TaskViewModel(application: Application) : AndroidViewModel(application){

    val googleId = GoogleSignIn.getLastSignedInAccount(application)
    val taskRepository = TaskRepository(googleId?.id.toString())

    val allTasksLiveData: LiveData<List<Task>> = taskRepository.allTasks

        init {
            Log.d("GoogleIdInVM", googleId.toString())
            taskRepository.getListOfTasks()
        }

    fun insertNewTask(task:Task,id:String) {
        taskRepository.insertNewTask(task,id)
    }

    fun deleteTask(task:Task,id:String) {
        taskRepository.deleteNote(task,id)
    }
}
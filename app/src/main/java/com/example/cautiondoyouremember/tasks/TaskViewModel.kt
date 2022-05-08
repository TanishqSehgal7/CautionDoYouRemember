package com.example.cautiondoyouremember.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cautiondoyouremember.notes.Note
import com.example.cautiondoyouremember.notes.NoteRepository

class TaskViewModel(val googleId: String,
 private var taskRepository: TaskRepository = TaskRepository(googleId))
    : ViewModel(){

    val allTasksLiveData: LiveData<List<Task>> = taskRepository.getTasks()

        init {
            taskRepository = TaskRepository(googleId)
//            allTasksLiveData = taskRepository.allTasks
        }

    fun insertNewTask(task:Task,id:String) {
        taskRepository.insertNewTask(task,id)
    }

    fun deleteTask(task:Task,id:String) {
        taskRepository.deleteNote(task,id)
    }

    fun taskResponseFromFirebaseAsMutableLiveData(): LiveData<List<Task>> {
        return taskRepository.getListOfTasks()
    }


}
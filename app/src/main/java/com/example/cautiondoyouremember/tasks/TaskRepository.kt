package com.example.cautiondoyouremember.tasks
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TaskRepository(private val googleId: String) {

    private val rootReferenceForTasks: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val taskReference: DatabaseReference = rootReferenceForTasks.child(googleId).child("Tasks")

    val allTasks: MutableLiveData<TaskResponse> = taskResponseFromFirebaseAsMutableLiveData()

    fun insertNewTask(task:Task, id:String) {
        task.id=id
        taskReference.child(task.id).child("TaskTitle").setValue(task.taskTitle)
        taskReference.child(task.id).child("TaskDescription").setValue(task.taskDescription)
        taskReference.child(task.id).child("TaskDate").setValue(task.time)
        taskReference.child(task.id).child("TaskStatus").setValue(false)

    }

    fun deleteNote(task: Task, id:String) {
        taskReference.child(task.id).removeValue()
    }


    fun taskResponseFromFirebaseAsMutableLiveData(): MutableLiveData<TaskResponse> {

        val mutableLiveDataForNotes = MutableLiveData<TaskResponse>()
        taskReference.get().addOnCompleteListener { task->
            val response = TaskResponse()
            if (task.isSuccessful) {
                val result = task.result
                result.let {
                    response.tasks = result.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Task::class.java)!!
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
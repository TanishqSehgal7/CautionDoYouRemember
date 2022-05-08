package com.example.cautiondoyouremember.tasks
import android.renderscript.Sampler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import kotlin.math.absoluteValue

class TaskRepository(private val googleId: String) {

    private val rootReferenceForTasks: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val taskReference: DatabaseReference = rootReferenceForTasks.child(googleId).child("Tasks")

    var allTasks = MutableLiveData<List<Task>>()

    init {
        Log.d("GoogleIdInTasks", googleId)
    }

    fun insertNewTask(task:Task, id:String) {
        taskReference.child(id).child("TaskTitle").setValue(task.TaskTitle)
        taskReference.child(id).child("TaskDescription").setValue(task.TaskDescription)
        taskReference.child(id).child("TaskDate").setValue(task.TaskTime)
        taskReference.child(id).child("TaskStatus").setValue(false)
    }

    fun deleteNote(task: Task, id:String) {
        taskReference.child(id).removeValue()
    }

    fun getListOfTasks() {
        val listTask = arrayListOf<Task>()
        taskReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTask.clear()
                Log.d("Repo", "${snapshot.childrenCount}")
                for (taskItems in snapshot.children) {
                    val taskItem = taskItems.getValue(Task::class.java)
                    taskItem?.let { listTask.add(it) }
                }
                listTask.sortByDescending { it.TaskTime?.toLong()?.absoluteValue }
                allTasks.postValue(listTask)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
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
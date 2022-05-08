package com.example.cautiondoyouremember.tasks
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cautiondoyouremember.notes.Note
import com.google.firebase.database.*

class TaskRepository(private val googleId: String) {

    private val rootReferenceForTasks: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private val taskReference: DatabaseReference = rootReferenceForTasks.child(googleId).child("Tasks")

//    val allTasks: MutableLiveData<TaskResponse> = taskResponseFromFirebaseAsMutableLiveData()
    var allTasks: MutableLiveData<List<Task>>? = null

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

    fun getTasks() : LiveData<List<Task>> {
        if (allTasks==null) {
            allTasks = MutableLiveData()
            getListOfTasks()
        }
        return allTasks!!
    }

    fun getListOfTasks() : LiveData<List<Task>> {
        allTasks = MutableLiveData()
        val list = arrayListOf<Task>()

        taskReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (noteItems in snapshot.children) {
                    val noteItem = noteItems.getValue(Task::class.java)
                    noteItem?.let { list.add(it) }
                }
                allTasks?.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TaskList", error.toString())
            }
        })
        return allTasks!!
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
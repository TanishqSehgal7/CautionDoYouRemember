package com.example.cautiondoyouremember.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.cautiondoyouremember.databinding.ActivityAddTaskBinding
import com.example.cautiondoyouremember.tasks.Task
import com.example.cautiondoyouremember.tasks.TaskRepository
import com.example.cautiondoyouremember.tasks.TaskViewModel
import com.example.cautiondoyouremember.tasks.TaskViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dmax.dialog.SpotsDialog
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddTaskBinding
    private lateinit var viewModel:TaskViewModel
    private lateinit var taskId:String
    private lateinit var waitingDialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskId = UUID.randomUUID().toString()

        binding.backbtnTaskAct.setOnClickListener {
            finish()
        }
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val taskRepository= TaskRepository(acct?.id.toString())

        viewModel=ViewModelProvider(this,TaskViewModelFactory(taskRepository,this))
            .get(TaskViewModel::class.java)

        binding.saveTask.setOnClickListener {
            val titleOfTask = binding.NoteTitle.text.toString()
            val descOfTask = binding.NoteDesc.text.toString()
            val dateOfTask = System.currentTimeMillis()

            val task = Task(taskId)
            task.taskTitle = titleOfTask
            task.taskDescription = descOfTask
            task.date = dateOfTask
            task.status = false

            if (acct!=null) {
                viewModel.insertNewTask(task,taskId)
                waitingDialog = SpotsDialog.Builder().setContext(this)
                    .setMessage("Saving Task...")
                    .setCancelable(true)
                    .build().apply {
                        show()
                        Handler().postDelayed(object : Runnable {
                            override fun run() {
                                waitingDialog.dismiss()
                                finish()
                            }
                        }, 2000)
                    }
            }

        }


    }
}
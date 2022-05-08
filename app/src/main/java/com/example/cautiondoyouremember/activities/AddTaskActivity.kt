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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
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

        val dateLong = System.currentTimeMillis()
        val date = dateLong.let { Date(it) }
        val format =  SimpleDateFormat("dd/MM/yyyy @ hh:mm a")
        val reminderDate = format.format(date)

        binding.dateInTask.text = reminderDate

        viewModel=ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.saveTask.setOnClickListener {

            if (binding.TaskTitle.text.isEmpty() || binding.TaskDesc.text.isEmpty()) {
                binding.TaskTitle.requestFocus()
                binding.TaskDesc.requestFocus()
                binding.TaskTitle.error = "Title and Description can't be empty!"
            }

            val titleOfTask = binding.TaskTitle.text.toString()
            val descOfTask = binding.TaskDesc.text.toString()
            val timeOfTask = System.currentTimeMillis().toString()
            val statusOfTask = false

            val task = Task(titleOfTask, descOfTask,  statusOfTask, timeOfTask)

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
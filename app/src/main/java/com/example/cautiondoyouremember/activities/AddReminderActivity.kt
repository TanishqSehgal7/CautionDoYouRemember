package com.example.cautiondoyouremember.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.example.cautiondoyouremember.databinding.ActivityAddReminderBinding
import com.example.cautiondoyouremember.reminders.Reminder
import com.example.cautiondoyouremember.reminders.ReminderRepository
import com.example.cautiondoyouremember.reminders.ReminderViewModel
import com.example.cautiondoyouremember.reminders.ReminderViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dmax.dialog.SpotsDialog
import java.util.*

class AddReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReminderBinding
    private lateinit var viewModel:ReminderViewModel
    private lateinit var reminderId:String
    private lateinit var waitingDialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reminderId = UUID.randomUUID().toString()

        binding.backbtnReminderAct.setOnClickListener {
            finish()
        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val reminderRepository = ReminderRepository(acct?.id.toString())

        viewModel = ViewModelProvider(this,ReminderViewModelFactory(reminderRepository,this))
            .get(ReminderViewModel::class.java)

        binding.saveRemider.setOnClickListener {
            val reminderDesc = binding.reminderDesc.text.toString()
            val reminderTime = System.currentTimeMillis()
            val isReminderCompletedStatus = false

            val reminder = Reminder(reminderId)
            reminder.reminderDescription = reminderDesc
            reminder.reminderTime = reminderTime
            reminder.isreminderCompletedStatus = isReminderCompletedStatus

            if (acct!==null) {
                viewModel.insertNewReminder(reminder,reminderId)
                waitingDialog = SpotsDialog.Builder().setContext(this)
                    .setMessage("Saving Reminder...")
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
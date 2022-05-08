package com.example.cautiondoyouremember.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.adapters.ReminderAdapter
import com.example.cautiondoyouremember.databinding.ActivityFaceRecognitionResultIntentBinding
import com.example.cautiondoyouremember.reminders.Reminder
import com.example.cautiondoyouremember.reminders.ReminderRepository
import com.example.cautiondoyouremember.reminders.ReminderViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dmax.dialog.SpotsDialog

class FaceRecognitionResultIntent : AppCompatActivity() {

    lateinit var reminderRecyclerView: RecyclerView
    lateinit var reminderAdapter: ReminderAdapter
    private lateinit var viewModel: ReminderViewModel
    private lateinit var allReminders: ArrayList<Reminder>
    private lateinit var binding: ActivityFaceRecognitionResultIntentBinding
    private lateinit var waitingDialog : android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceRecognitionResultIntentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // display all reminders after notification comes
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val reminderRepository = ReminderRepository(acct?.id.toString())
        reminderRecyclerView = binding.rvTargetIntent

        // back button
        binding.backBtnTargetIntent.setOnClickListener {
            finish()
        }

        viewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        allReminders = ArrayList<Reminder>()

        val arrayListOfMessage:String = "#Turn Off Air Conditioner and other electrical appliances \n # Take necessary things like mobile phone, wallet, car keys etc"

        viewModel.allReminderLiveData.observe(this) {
            waitingDialog = SpotsDialog.Builder().setContext(this)
                .setMessage("Before you setp out, kindly keep the following things in mind:\n" +
                    arrayListOfMessage + "\n\n You can refer to the reminders you need to keep a track of as well")
                .setCancelable(true)
                .build().apply {
                    show()
                    Handler().postDelayed(object : Runnable {
                        override fun run() {
                            waitingDialog.dismiss()
                        }
                    }, 8000)
                }
            reminderRecyclerView.setHasFixedSize(true)
            reminderRecyclerView.layoutManager = LinearLayoutManager(this)
            allReminders = it as ArrayList<Reminder>
            reminderAdapter = ReminderAdapter(allReminders)
            reminderRecyclerView.adapter = reminderAdapter
            Log.d("AllReminders", it.toString())
        }
    }
}
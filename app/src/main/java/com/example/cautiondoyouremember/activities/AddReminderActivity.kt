package com.example.cautiondoyouremember.activities

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.example.cautiondoyouremember.databinding.ActivityAddReminderBinding
import com.example.cautiondoyouremember.receivers.AlarmBroascastReceiverForReminders
import com.example.cautiondoyouremember.reminders.Reminder
import com.example.cautiondoyouremember.reminders.ReminderRepository
import com.example.cautiondoyouremember.reminders.ReminderViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AddReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReminderBinding
    private lateinit var viewModel:ReminderViewModel
    private lateinit var reminderId:String
    private lateinit var waitingDialog: android.app.AlertDialog
    lateinit var timePicker: MaterialTimePicker
    lateinit var calendar: Calendar
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    private var reminderTime by Delegates.notNull<Long>()
    private var reminderDesc by Delegates.notNull<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reminderId = UUID.randomUUID().toString()

        binding.backbtnReminderAct.setOnClickListener {
            finish()
        }

        val dateLong = System.currentTimeMillis()
        val date = dateLong.let { Date(it) }
        val format =  SimpleDateFormat("dd/MM/yyyy @ hh:mm a")
        val reminderDate = format.format(date)
        binding.dateInReminder.text = reminderDate

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val reminderRepository = ReminderRepository(acct?.id.toString())

        viewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        binding.scheduleAlarm.setOnClickListener {
            showTimePicker()
        binding.saveRemider.setOnClickListener {
            if (binding.reminderDesc.text?.isEmpty() == true) {
                binding.reminderDesc.requestFocus()
                binding.reminderDesc.error = "Reminder Description Can't be empty!"

            } else {
                reminderDesc = binding.reminderDesc.text.toString()
                intent.putExtra("notificationText",binding.reminderDesc.text.toString())
                val isReminderCompletedStatus = false

                val reminder = Reminder(reminderDesc, reminderTime.toString(),isReminderCompletedStatus)

                    viewModel.insertNewReminder(reminder, reminderId)
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
            setAlarm()
        }
    }
        val intent = Intent()
        intent.putExtra("notificationText",reminderDesc)
        createNotificationChannelForAlarm()
    }

    private fun createNotificationChannelForAlarm() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channelName: CharSequence = "NotificationForReminders"
            val description = "Channel for the Reminder Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel("alarmNotificationForNote", channelName, importance)
            notificationChannel.description = description

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun showTimePicker() {
        timePicker=MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time")
            .build()

        timePicker.show(supportFragmentManager,"alarmNotificationForNote")
        timePicker.addOnPositiveButtonClickListener {
            calendar= Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY,timePicker.hour)
            calendar.set(Calendar.MINUTE,timePicker.minute)
            calendar.set(Calendar.SECOND,0)
            calendar.set(Calendar.MILLISECOND,0)
            reminderTime = calendar.timeInMillis
//            val broadcastIntent = Intent(application, AlarmBroascastReceiverForReminders::class.java)
//            sendBroadcast(broadcastIntent)
        }
    }

    fun setAlarm() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,AlarmBroascastReceiverForReminders::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(this, 0,intent,PendingIntent.FLAG_IMMUTABLE)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(this, 0,intent,PendingIntent.FLAG_MUTABLE)
        }
//        pendingIntent = PendingIntent.getBroadcast(this, 0,intent,PendingIntent.FLAG_MUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,reminderTime,pendingIntent)
    }

}
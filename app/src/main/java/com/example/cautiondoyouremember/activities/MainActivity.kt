package com.example.cautiondoyouremember.activities

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.adapters.AdapterForSwipableViews
import com.example.cautiondoyouremember.databinding.ActivityMainBinding
import com.example.cautiondoyouremember.receivers.AlarmBroascastReceiverForFaceRecognition
import com.example.cautiondoyouremember.reminders.FaceRecoognitionData
import com.example.cautiondoyouremember.reminders.ReminderRepository
import com.example.cautiondoyouremember.reminders.ReminderViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapterForSwipableViews: AdapterForSwipableViews
    private lateinit var tabLayout: TabLayout
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    private lateinit var viewModel: ReminderViewModel

    private val rootReferenceForReminders: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    private lateinit var binding : ActivityMainBinding
    val THRESHOLD_TIME_FOR_FACE_RECOGNITION = 20*1000
    private lateinit var faceRecognitionArrayList: ArrayList<FaceRecoognitionData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        val settingBtn = findViewById<ImageButton>(R.id.settings)

        viewPager2 = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        adapterForSwipableViews = AdapterForSwipableViews(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapterForSwipableViews
        TabLayoutMediator(tabLayout,viewPager2) { tab, position ->
            when(position) {
                0 -> {
                  tab.text = "Notes"
                }
                1-> {
                    tab.text = "Tasks"
                }
                2 -> {
                    tab.text = "Reminders"
                }
            }
        }.attach()

        settingBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // FAB control of activities
        binding.addNote.setOnClickListener {
            startActivity(Intent(this,AddNoteActivity::class.java))
        }
        binding.addTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
        binding.addReminder.setOnClickListener {
            startActivity(Intent(this, AddReminderActivity::class.java))
        }

        // face recognition data
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val reminderRepository = ReminderRepository(acct?.id.toString())
//        val faceRecordReference = rootReferenceForReminders.child(acct?.id.toString()).child("FaceRecognitionRecords")
        faceRecognitionArrayList = ArrayList<FaceRecoognitionData>()

        createNotificationChannelForAlarm()

        viewModel.allFaceRecognitionRecords.observe(this) {
            faceRecognitionArrayList = it as ArrayList<FaceRecoognitionData>

            for (faceData in it) {
                if (System.currentTimeMillis() < faceData.whenFaceGotRecognized + THRESHOLD_TIME_FOR_FACE_RECOGNITION || faceData.notificationStatus) {
                    Log.d("ConditionSatisfied", "Notification Triggered")
                    setAlarm()
                }
            }
        }
    }


    private fun createNotificationChannelForAlarm() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channelName: CharSequence = "NotificationForFaceRecognition"
            val description = "Channel for the FaceRecognition Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel("alarmNotificationForFaceRecognition", channelName, importance)
            notificationChannel.description = description

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            Log.d("ConditionSatisfied", "Notification Channel Created")
        }
    }

    fun setAlarm() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroascastReceiverForFaceRecognition::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(this, 0,intent, PendingIntent.FLAG_IMMUTABLE)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(this, 0,intent, PendingIntent.FLAG_MUTABLE)
        }
//        pendingIntent = PendingIntent.getBroadcast(this, 0,intent,PendingIntent.FLAG_MUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pendingIntent)
        Log.d("ConditionSatisfied", "Notification Sent")
    }

}


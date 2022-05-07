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
import androidx.viewpager2.widget.ViewPager2
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.adapters.AdapterForSwipableViews
import com.example.cautiondoyouremember.databinding.ActivityMainBinding
import com.example.cautiondoyouremember.receivers.AlarmBroascastReceiverForFaceRecognition
import com.example.cautiondoyouremember.reminders.FaceRecoognitionData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapterForSwipableViews: AdapterForSwipableViews
    private lateinit var tabLayout: TabLayout
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent

    private val rootReferenceForReminders: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    private lateinit var binding : ActivityMainBinding
    val THRESHOLD_TIME_FOR_FACE_RECOGNITION = 30*1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val faceRecordReference = rootReferenceForReminders.child(acct?.id.toString()).child("FaceRecognitionRecords")
        val faceRecognitionArrayList: ArrayList<FaceRecoognitionData> = ArrayList<FaceRecoognitionData>()

        faceRecordReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (faceRecogRecord in snapshot.children) {
                        val whoGotRecognized = faceRecogRecord.child("WhoGotRecognized").value as String
                        val notificationStatus = faceRecogRecord.child("notificationStatus").value as Boolean
                        val recognitionTime = faceRecogRecord.child("RecognitionTime").value as Long

                        val faceRecogData = FaceRecoognitionData(whoGotRecognized, recognitionTime, notificationStatus)
                        faceRecognitionArrayList.add(faceRecogData)
                        Log.d("FaceRecognitionData", faceRecogRecord.toString())

                        if (System.currentTimeMillis() <= recognitionTime + THRESHOLD_TIME_FOR_FACE_RECOGNITION ) {
                            setAlarm()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
    }

}


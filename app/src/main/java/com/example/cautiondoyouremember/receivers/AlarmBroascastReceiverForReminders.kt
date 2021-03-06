package com.example.cautiondoyouremember.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.activities.MainActivity


class AlarmBroascastReceiverForReminders : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val intentTarget = Intent(context, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(context, 0, intentTarget, PendingIntent.FLAG_MUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           pendingIntent = PendingIntent.getActivity(context, 0, intentTarget, PendingIntent.FLAG_IMMUTABLE)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(context, 0, intentTarget, PendingIntent.FLAG_MUTABLE)
        }
        val notificationBuilder = context?.let { NotificationCompat.Builder(it, "alarmNotificationForNote") }
//        val notificationText=intent?.getStringExtra("notificationText")

        val getNotificationText = intent?.extras?.getString("ReminderNotiText")
        Log.d("notificationText", getNotificationText.toString())

        notificationBuilder?.setSmallIcon(R.drawable.ic_baseline_alarm_24)
            ?.setContentTitle("Reminder!!")
            ?.setContentText("You have got a reminder. Click to Check")
            ?.setAutoCancel(true)
            ?.setDefaults(NotificationCompat.DEFAULT_ALL)?.priority = NotificationCompat.PRIORITY_MAX

        val alarmSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationBuilder?.setSound(alarmSound)?.setColor(ContextCompat.getColor(context,R.color.Yellow))
        notificationBuilder?.setContentIntent(pendingIntent)

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        if (notificationBuilder != null) {
            notificationManager?.notify(123, notificationBuilder.build())
        }
    }
}
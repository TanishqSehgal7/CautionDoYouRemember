package com.example.cautiondoyouremember.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.activities.FaceRecognitionResultIntent


class AlarmBroascastReceiverForFaceRecognition: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val intentTarget = Intent(context, FaceRecognitionResultIntent::class.java)
        var pendingIntent = PendingIntent.getActivity(context,0,intentTarget, PendingIntent.FLAG_MUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(context, 0, intentTarget, PendingIntent.FLAG_IMMUTABLE)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(context, 0, intentTarget, PendingIntent.FLAG_MUTABLE)
        }

        val notificationBuilder = context?.let { NotificationCompat.Builder(it, "alarmNotificationForFaceRecognition") }
        notificationBuilder?.setSmallIcon(R.drawable.ic_baseline_alarm_on_24)
            ?.setContentTitle("Reminder!!")
            ?.setContentText("Before you step out, kindly refer the reminders that you might need to keep a track of. \n " +
                    "Click to check reminders.")
            ?.setAutoCancel(true)
            ?.setDefaults(NotificationCompat.DEFAULT_ALL)?.priority = NotificationCompat.PRIORITY_MAX

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationBuilder?.setSound(alarmSound)?.setColor(ContextCompat.getColor(context,R.color.Red))
        notificationBuilder?.setContentIntent(pendingIntent)

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        if (notificationBuilder != null) {
            notificationManager?.notify(123, notificationBuilder.build())
        }
    }
}
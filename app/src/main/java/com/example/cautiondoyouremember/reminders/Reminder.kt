package com.example.cautiondoyouremember.reminders

data class Reminder(
    val ReminderDescription:String?=null,
    val ReminderTime:String?=null,
    var isreminderCompletedStatus:Boolean=false)
//    var isFaceRecognized:Boolean=false
//    var whenFaceRecognized:String?=null
//    var whoGotRecognized:String?=null
//    var notificationStatus:Boolean=false
//    var isReminderMarked:Boolean=false


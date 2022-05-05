package com.example.cautiondoyouremember.reminders

data class Reminder(var id:String="") {
    var reminderDescription:String?=null
    var reminderTime:Long = 0L
    var isreminderCompletedStatus:Boolean=false
    var isFaceRecognized:Boolean=false
    var whenFaceRecognized:Long=0L
    var whoGotRecognized:String?=null
    var notificationStatus:Boolean=false
    var isReminderMarked:Boolean=false
}

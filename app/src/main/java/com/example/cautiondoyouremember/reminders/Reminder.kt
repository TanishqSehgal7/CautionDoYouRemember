package com.example.cautiondoyouremember.reminders

data class Reminder(var id:String="") {
    var reminderDescription:String?=null
    var reminderTime:String?=null
    var isreminderCompletedStatus:Boolean=false
//    var isFaceRecognized:Boolean=false
//    var whenFaceRecognized:String?=null
//    var whoGotRecognized:String?=null
//    var notificationStatus:Boolean=false
//    var isReminderMarked:Boolean=false
}

package com.example.cautiondoyouremember.reminders

data class Reminder(var id:String="") {
    var reminder:String?=null
    var reminderTime:String?=null
    var isFaceRecognized:Boolean=false
    var whenFaceRecognized:Long=0L
    var whoGotRecognized:String?=null
    var notificationStatus:Boolean=false
    var isReminderMarked:Boolean=false
    var reminderDate:Long=0L
}

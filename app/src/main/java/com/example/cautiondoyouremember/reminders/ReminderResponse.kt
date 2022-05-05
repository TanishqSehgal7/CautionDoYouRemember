package com.example.cautiondoyouremember.reminders

import java.lang.Exception

data class ReminderResponse(
    var reminders:List<Reminder>?=null,
    var exception: Exception?=null
)
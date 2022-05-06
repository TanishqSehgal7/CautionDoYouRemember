package com.example.cautiondoyouremember.tasks

data class Task(var id:String="") {
    var taskTitle:String?=null
    var taskDescription:String?=null
    var status:Boolean = false
    var time:String?=null
}

package com.example.cautiondoyouremember.tasks

data class Task(var id:String="", val taskTitle:String?=null,
                val taskDescription:String?=null,
                val status:Boolean = false,
                val time:String?=null)

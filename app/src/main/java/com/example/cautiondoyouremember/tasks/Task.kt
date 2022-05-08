package com.example.cautiondoyouremember.tasks

data class Task(val TaskTitle:String?=null,
                val TaskDescription:String?=null,
                val Status:Boolean = false,
                val TaskTime:String?=null)

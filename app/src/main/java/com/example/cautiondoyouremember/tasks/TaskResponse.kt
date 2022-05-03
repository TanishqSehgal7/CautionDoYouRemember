package com.example.cautiondoyouremember.tasks

import java.lang.Exception

data class TaskResponse(
    var tasks:List<Task>?=null,
    var exception: Exception?=null
)

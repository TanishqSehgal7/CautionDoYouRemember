package com.example.cautiondoyouremember.notes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

data class Note(val id:String?=null,
                val noteTitle:String?=null,
                val noteDesc:String?=null,
                val time:String?=null)
//{


//    @Exclude
//    fun toMap():Map<String,Any?> {
//        return mapOf(
//            "noteTitle" to noteTitle,
//            "noteDesc" to noteDesc,
//            "time" to date
//        )
//    }

//}
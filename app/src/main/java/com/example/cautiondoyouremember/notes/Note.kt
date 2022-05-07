package com.example.cautiondoyouremember.notes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

data class Note(val id:String?="",
                val noteTitle:String?="",
                val noteDesc:String?="",
                val time:Long=0L) {

}
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
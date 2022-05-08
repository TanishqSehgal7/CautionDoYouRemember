package com.example.cautiondoyouremember.notes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

data class Note(
    val NoteDate: String? = null,
    val NoteDescription: String? = null,
    val NoteTitle: String? = null,
)
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
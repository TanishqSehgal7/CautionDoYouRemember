package com.example.cautiondoyouremember.notes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Note(var id:String="") {

    var noteTitle:String?=null
    var noteDesc:String?=null
    var date:Long=0L

    @Exclude
    fun toMap():Map<String,Any?> {
        return mapOf(
            "noteTitle" to noteTitle,
            "noteDesc" to noteDesc,
            "time" to date
        )
    }

}
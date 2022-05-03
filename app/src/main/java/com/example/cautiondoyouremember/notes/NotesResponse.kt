package com.example.cautiondoyouremember.notes

import java.lang.Exception

data class NotesResponse(
    var notes:List<Note>?=null,
    var exception: Exception?=null)

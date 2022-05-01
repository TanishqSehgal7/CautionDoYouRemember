package com.example.cautiondoyouremember.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtnNoteAct.setOnClickListener {
            finish()
        }
    }
}
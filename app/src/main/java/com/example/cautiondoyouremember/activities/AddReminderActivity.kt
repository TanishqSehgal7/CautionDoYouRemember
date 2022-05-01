package com.example.cautiondoyouremember.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cautiondoyouremember.databinding.ActivityAddReminderBinding

class AddReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtnReminderAct.setOnClickListener {
            finish()
        }
    }
}
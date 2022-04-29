package com.example.cautiondoyouremember.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.databinding.ActivityMainBinding
import com.example.cautiondoyouremember.databinding.ActivitySettingsBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backBtn = findViewById<ImageView>(R.id.back_btn_settings)

        backBtn.setOnClickListener {
            finish()
        }

        // log out from google
        firebaseAuth = Firebase.auth
        val logoutBtn = findViewById<Button>(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            finish()
        }
    }
}
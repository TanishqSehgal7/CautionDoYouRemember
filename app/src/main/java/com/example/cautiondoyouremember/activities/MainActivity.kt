package com.example.cautiondoyouremember.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.adapters.AdapterForSwipableViews
import com.example.cautiondoyouremember.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapterForSwipableViews: AdapterForSwipableViews
    private lateinit var tabLayout: TabLayout

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingBtn = findViewById<ImageButton>(R.id.settings)

        viewPager2 = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        adapterForSwipableViews = AdapterForSwipableViews(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapterForSwipableViews
        TabLayoutMediator(tabLayout,viewPager2) { tab, position ->
            when(position) {
                0 -> {
                  tab.text = "Notes"
                }
                1-> {
                    tab.text = "Tasks"
                }
                2 -> {
                    tab.text = "Reminders"
                }
            }
        }.attach()

        settingBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // FAB control of activities
        binding.addNote.setOnClickListener {
            startActivity(Intent(this,AddNoteActivity::class.java))
        }
        binding.addTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
        binding.addReminder.setOnClickListener {
            startActivity(Intent(this, AddReminderActivity::class.java))
        }
    }
}
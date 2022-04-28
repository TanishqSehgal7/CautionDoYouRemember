package com.example.cautiondoyouremember.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.adapters.AdapterForSwipableViews
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapterForSwipableViews: AdapterForSwipableViews
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }
}
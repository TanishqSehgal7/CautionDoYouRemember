package com.example.cautiondoyouremember.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cautiondoyouremember.fragments.NotesFragment
import com.example.cautiondoyouremember.fragments.RemindersFragment
import com.example.cautiondoyouremember.fragments.TasksFragment

class AdapterForSwipableViews(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> {
                NotesFragment()
            }
            1-> {
                TasksFragment()
            }
            2-> {
                RemindersFragment()
            }
            else -> {
                NotesFragment()
            }
        }
    }


}
package com.example.cautiondoyouremember.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.adapters.ReminderAdapter
import com.example.cautiondoyouremember.reminders.Reminder
import com.example.cautiondoyouremember.reminders.ReminderRepository
import com.example.cautiondoyouremember.reminders.ReminderViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RemindersFragment : Fragment() {

    lateinit var reminderRecyclerView: RecyclerView
    lateinit var reminderAdapter: ReminderAdapter
    private lateinit var viewModel: ReminderViewModel
    private lateinit var allReminders: ArrayList<Reminder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val acct = GoogleSignIn.getLastSignedInAccount(this.requireContext())
        val rootReferenceForNotes: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val noteReference: DatabaseReference = rootReferenceForNotes.child(acct?.id.toString()).child("Reminders")
        val view = inflater.inflate(R.layout.fragment_reminders, container, false)
        val reminderRepository = ReminderRepository(acct?.id.toString())
        reminderRecyclerView = view.findViewById(R.id.remindersRv)

        viewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        allReminders = ArrayList<Reminder>()

        viewModel.allReminderLiveData.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "$it[0]", Toast.LENGTH_SHORT).show()
            reminderRecyclerView.setHasFixedSize(true)
            reminderRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
            allReminders = it as ArrayList<Reminder>
            reminderAdapter = ReminderAdapter(allReminders)
            reminderRecyclerView.adapter = reminderAdapter
            Log.d("AllReminders", it.toString())
        }


        return view
    }
}
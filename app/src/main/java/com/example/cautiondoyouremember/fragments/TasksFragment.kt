package com.example.cautiondoyouremember.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.adapters.TaskAdapter
import com.example.cautiondoyouremember.notes.Note
import com.example.cautiondoyouremember.tasks.Task
import com.example.cautiondoyouremember.tasks.TaskRepository
import com.example.cautiondoyouremember.tasks.TaskViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TasksFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var allTasks:ArrayList<Task>
    lateinit var taskRecyclerView: RecyclerView
    lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val acct = GoogleSignIn.getLastSignedInAccount(this.requireContext())
        val rootReferenceForNotes: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val taskReference: DatabaseReference = rootReferenceForNotes.child(acct?.id.toString()).child("Tasks")

        val view:View = inflater.inflate(R.layout.fragment_tasks, container, false)
        val taskRepository = TaskRepository(acct?.id.toString())

        taskRecyclerView = view.findViewById(R.id.tasksRv)
        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        allTasks = ArrayList<Task>()

        viewModel.allTasksLiveData.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "$it[0]", Toast.LENGTH_SHORT).show()
            taskRecyclerView.setHasFixedSize(true)
            taskRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
            allTasks = it as ArrayList<Task>
            taskAdapter = TaskAdapter(allTasks)
            taskRecyclerView.adapter = taskAdapter
            Log.d("AllTasks", it.toString())
        }
        return view
    }
}
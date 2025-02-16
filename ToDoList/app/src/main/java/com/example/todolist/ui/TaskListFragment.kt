package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentTaskListBinding
import com.example.todolist.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.todolist.R

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabDelete: FloatingActionButton = binding.fabDelete
        fabDelete.visibility = View.GONE // בהתחלה מוסתר

        taskAdapter = TaskAdapter(
            onItemClick = { task ->
                // טיפול בלחיצה על אייטם אם נדרש
            },
            onCheckBoxClick = { selectedTasks ->
                // הצגת כפתור מחיקה רק אם יש פריטים מסומנים
                if (selectedTasks.isNotEmpty()) {
                    fabDelete.visibility = View.VISIBLE
                } else {
                    fabDelete.visibility = View.GONE
                }
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = taskAdapter

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks)
        }

        fabDelete.setOnClickListener {
            // מחיקת משימות מסומנות
            val selectedTasks = taskAdapter.selectedTasks
            selectedTasks.forEach { task ->
                viewModel.deleteTask(task)
            }
            taskAdapter.clearSelections()
            fabDelete.visibility = View.GONE
        }

        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_addEditTaskFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

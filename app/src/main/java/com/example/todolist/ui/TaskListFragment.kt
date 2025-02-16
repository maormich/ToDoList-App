package com.example.todolist.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentTaskListBinding
import com.example.todolist.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.todolist.model.Task

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
        fabDelete.visibility = View.GONE

        taskAdapter = TaskAdapter(
            onItemClick = { task ->
            },
            onCheckBoxClick = { selectedTasks ->
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

        binding.fabDelete.setOnClickListener {
            val selectedTasks = taskAdapter.selectedTasks
            if (selectedTasks.isNotEmpty()) {
                selectedTasks.forEach { task ->
                    showDeleteConfirmationDialog(task)
                }
                taskAdapter.clearSelections()
            } else {
                Toast.makeText(requireContext(), "No tasks selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(task: Task) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteTask(task)
                Toast.makeText(requireContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.todolist.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.model.Task
import com.example.todolist.viewmodel.TaskViewModel
import java.util.Calendar

class AddEditTaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private var selectedDeadline: Long = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_task, container, false)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        val titleInput = view.findViewById<EditText>(R.id.edit_text_title)
        val descriptionInput = view.findViewById<EditText>(R.id.edit_text_description)
        val saveButton = view.findViewById<View>(R.id.button_save_task)
        val deadlineButton = view.findViewById<View>(R.id.button_select_deadline)

        deadlineButton.setOnClickListener {
            showDateTimePicker()
        }

        saveButton.setOnClickListener {
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()

            if (title.isNotEmpty()) {
                val task = Task(
                    title = title,
                    description = description,
                    deadline = selectedDeadline,
                    isCompleted = false
                )
                taskViewModel.insert(task)

                Toast.makeText(requireContext(), "Task Saved", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Title is required", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        selectedDeadline = calendar.timeInMillis
                        Toast.makeText(requireContext(), "Deadline selected", Toast.LENGTH_SHORT).show()
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

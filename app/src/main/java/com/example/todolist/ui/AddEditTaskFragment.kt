package com.example.todolist.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.model.Task
import com.example.todolist.viewmodel.TaskViewModel
import java.util.Calendar

class AddEditTaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private var selectedDeadline: Long = 0L
    private var selectedImageUri: String? = null

    private lateinit var imageTask: ImageView

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri.toString()
                imageTask.setImageURI(uri)
            } else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

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
        val imageButton = view.findViewById<View>(R.id.button_select_image)
        imageTask = view.findViewById(R.id.image_task)

        deadlineButton.setOnClickListener {
            showDateTimePicker()
        }

        imageButton.setOnClickListener {
            openImagePicker()
        }

        saveButton.setOnClickListener {
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()

            if (title.isNotEmpty()) {
                val task = Task(
                    title = title,
                    description = description,
                    deadline = selectedDeadline,
                    imageUri = selectedImageUri ?: "ic_placeholder_image",
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

    private fun openImagePicker() {
        imagePickerLauncher.launch("image/*")
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

                        if (calendar.timeInMillis <= System.currentTimeMillis()) {
                            Toast.makeText(
                                requireContext(),
                                "Cannot select a past time. Defaulting to now.",
                                Toast.LENGTH_SHORT
                            ).show()
                            selectedDeadline = System.currentTimeMillis()
                        } else {
                            selectedDeadline = calendar.timeInMillis
                            Toast.makeText(requireContext(), "Deadline selected", Toast.LENGTH_SHORT).show()
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }
}

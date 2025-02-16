package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.todolist.databinding.FragmentTaskDetailBinding
import com.example.todolist.viewmodel.TaskViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R

class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        val taskId = requireArguments().getInt("taskId")

        viewModel.getTaskById(taskId).observe(viewLifecycleOwner) { task ->
            if (task != null) {
                binding.textViewTaskTitleDetail.text = task.title
                binding.textViewTaskDescriptionDetail.text = task.description ?: "No description"
                binding.textViewTaskDeadlineDetail.text =
                    if (task.deadline > 0) "Deadline: ${formatDeadline(task.deadline)}"
                    else "Deadline: Not Set"

                Glide.with(requireContext())
                    .load(task.imageUri ?: R.drawable.ic_placeholder_image)
                    .into(binding.imageViewTaskDetail)
            }
        }
    }

    private fun formatDeadline(deadline: Long): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(deadline))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

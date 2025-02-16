package com.example.todolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.model.Task
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class TaskAdapter(
    private val onItemClick: (Task) -> Unit,
    private val onCheckBoxClick: (Set<Task>) -> Unit // עדכון כאשר משתנים פריטים מסומנים
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList: List<Task> = emptyList()
    val selectedTasks = mutableSetOf<Task>()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.text_view_title)
        val descriptionText: TextView = itemView.findViewById(R.id.text_view_description) // שדה חדש לתיאור
        val deadlineText: TextView = itemView.findViewById(R.id.text_view_deadline)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_task)
        val taskImage: ImageView = itemView.findViewById(R.id.image_view_task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.titleText.text = task.title
        holder.descriptionText.text = task.description ?: "No description available" // הצגת התיאור
        holder.deadlineText.text = "Deadline: ${
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(task.deadline))
        }"
        holder.checkBox.isChecked = selectedTasks.contains(task)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedTasks.add(task)
            } else {
                selectedTasks.remove(task)
            }
            onCheckBoxClick(selectedTasks)
        }

        holder.itemView.setOnClickListener {
            onItemClick(task)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun submitList(tasks: List<Task>) {
        taskList = tasks
        notifyDataSetChanged()
    }

    fun clearSelections() {
        selectedTasks.clear()
        notifyDataSetChanged()
    }
}

package com.example.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.Task
import com.example.todolist.repository.TaskRepository
import com.example.todolist.model.AppDatabase
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val tasks: LiveData<List<Task>> // הצגת כל המשימות ב-LiveData

    init {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        tasks = repository.allTasks.asLiveData() // ממיר Flow ל-LiveData
    }

    fun insert(task: Task) = viewModelScope.launch {
        repository.insertTask(task) // הכנס משימה ל-Database
    }

    fun updateTaskCompletion(task: Task, isCompleted: Boolean) = viewModelScope.launch {
        val updatedTask = task.copy(isCompleted = isCompleted)
        repository.updateTask(updatedTask)
    }
    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

}

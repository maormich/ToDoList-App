package com.example.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.Task
import com.example.todolist.data.TaskRepository
import com.example.todolist.data.AppDatabase
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val tasks: LiveData<List<Task>>

    init {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        tasks = repository.allTasks.asLiveData()
    }

    fun insert(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    suspend fun getTaskById(taskId: Int): LiveData<Task> {
        return repository.getTaskById(taskId)
    }
}

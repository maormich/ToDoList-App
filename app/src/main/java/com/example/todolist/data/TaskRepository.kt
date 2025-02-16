package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.todolist.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks().asLiveData()

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)  // Room כבר מטפל בזה ב-Dispatchers.IO
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun getTaskById(taskId: Int): LiveData<Task> {
        return taskDao.getTaskById(taskId).asLiveData()
    }
}

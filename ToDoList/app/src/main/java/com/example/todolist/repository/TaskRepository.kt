package com.example.todolist.repository

import com.example.todolist.model.Task
import com.example.todolist.model.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }

    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.updateTask(task)
        }
    }

    suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }
}

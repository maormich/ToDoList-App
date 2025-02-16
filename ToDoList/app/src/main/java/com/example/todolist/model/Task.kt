package com.example.todolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val deadline: Long,
    val isCompleted: Boolean = false,
    val imageUri: String? = null
)

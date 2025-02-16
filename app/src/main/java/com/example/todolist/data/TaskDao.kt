package com.example.todolist.data

import androidx.room.*
import com.example.todolist.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task): Int

    @Delete
    suspend fun deleteTask(task: Task): Int

    @Query("DELETE FROM tasks WHERE id IN (:taskIds)")
    suspend fun deleteTasks(taskIds: List<Int>): Int

    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Int): Flow<Task>
}

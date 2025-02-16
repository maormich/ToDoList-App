package com.example.todolist.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // הוספת משימה - מחזירה את ה-ID של המשימה שנוספה
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task): Long

    // עדכון משימה - מחזירה את מספר השורות שעודכנו
    @Update
    fun updateTask(task: Task): Int

    // מחיקת משימה - מחזירה את מספר השורות שנמחקו
    @Delete
    fun deleteTask(task: Task): Int

    // מחיקת מספר משימות לפי IDs - מחזירה את מספר השורות שנמחקו
    @Query("DELETE FROM tasks WHERE id IN (:taskIds)")
    fun deleteTasks(taskIds: List<Int>): Int

    // קבלת כל המשימות - מחזירה רשימה של משימות (Flow)
    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    fun getAllTasks(): Flow<List<Task>>
}

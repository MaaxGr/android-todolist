package com.grossmax.androidtodolist.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.grossmax.androidtodolist.data.database.entity.ToDoEntity
import kotlinx.datetime.LocalDateTime

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo")
    fun getAll(): List<ToDoEntity>

    @Insert
    fun insertAll(vararg todo: ToDoEntity)

    @Delete
    fun delete(todo: ToDoEntity)

    @Query("UPDATE todo SET checked_at = :localDateTime WHERE uid = :id")
    fun setChecked(id: Int, localDateTime: LocalDateTime)

    @Query("UPDATE todo SET title = :title WHERE uid = :id")
    fun setTitle(id: Int, title: String)

    @Query("UPDATE todo SET checked_at = null WHERE uid = :id")
    fun setUnchecked(id: Int)

}
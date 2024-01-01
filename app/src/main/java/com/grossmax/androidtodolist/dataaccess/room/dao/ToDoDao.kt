package com.grossmax.androidtodolist.dataaccess.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.grossmax.androidtodolist.dataaccess.room.entity.ToDoEntity
import kotlinx.datetime.LocalDateTime

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo")
    fun getAll(): List<ToDoEntity>

    @Insert
    fun insertAll(vararg todo: ToDoEntity)

    @Query("DELETE FROM todo WHERE uid = :id")
    fun deleteById(id: Int)

    @Query("UPDATE todo SET checked_at = :localDateTime WHERE uid = :id")
    fun setChecked(id: Int, localDateTime: LocalDateTime)

    @Query("UPDATE todo SET title = :title WHERE uid = :id")
    fun setTitle(id: Int, title: String)

    @Query("UPDATE todo SET checked_at = null WHERE uid = :id")
    fun setUnchecked(id: Int)

}
package com.grossmax.androidtodolist.dataaccess.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "todo")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "checked_at")
    val checkedAt: LocalDateTime?,
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,
)
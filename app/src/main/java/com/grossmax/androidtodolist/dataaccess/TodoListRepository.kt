package com.grossmax.androidtodolist.dataaccess

import kotlinx.datetime.LocalDateTime

interface TodoListRepository {

    data class ToDoItem(
        val uid: Int,
        val title: String,
        val checkedAt: LocalDateTime?,
        val createdAt: LocalDateTime,
    )

    fun loadToDoListFromMemory(): List<ToDoItem>

    fun addTodoItem(item: ToDoItem)

    fun addChangeListener(listener: () -> Unit)
    fun triggerChange()
    fun setChecked(id: Int, localDateTime: LocalDateTime)
    fun setUnchecked(id: Int)
    fun setTitle(id: Int, title: String)
    fun deleteById(id: Int)
}
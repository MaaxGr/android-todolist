package com.grossmax.androidtodolist.data

interface TodoListRepository {

    data class ToDoItem(
        val title: String
    )

    fun getToDoList(): List<ToDoItem>

    fun addTodoItem(item: ToDoItem)


    fun addChangeListener(listener: () -> Unit)
    fun triggerChange()
}
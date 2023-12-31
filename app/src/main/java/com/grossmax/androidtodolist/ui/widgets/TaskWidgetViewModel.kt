package com.grossmax.androidtodolist.ui.widgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grossmax.androidtodolist.businesslogic.services.TimeServer
import com.grossmax.androidtodolist.dataaccess.TodoListRepository
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskWidgetViewModel: ViewModel() {

    private val timeServer: TimeServer by koinInject()
    private val todoListRepository: TodoListRepository by koinInject()

    val todoItems: MutableStateFlow<List<TodoListRepository.ToDoItem>> = MutableStateFlow(listOf())

    fun loadTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoList = todoListRepository.loadToDoListFromMemory()
                .sortedByDescending { it.uid }

            todoItems.value = todoList.filter { it.checkedAt == null }
        }
    }

    fun checkTask(toDoEntity: TodoListRepository.ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.setChecked(
                id = toDoEntity.uid,
                localDateTime = timeServer.getCurrentLocalDateTime()
            )
            loadTodoList()
            todoListRepository.triggerChange()
        }
    }

    fun addTodoItem(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.addTodoItem(
                TodoListRepository.ToDoItem(
                    uid = 0,
                    title = text,
                    checkedAt = null,
                    createdAt = timeServer.getCurrentLocalDateTime()
                )
            )
        }
    }

}
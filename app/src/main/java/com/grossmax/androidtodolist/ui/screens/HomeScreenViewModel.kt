package com.grossmax.androidtodolist.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grossmax.androidtodolist.businesslogic.services.TimeServer
import com.grossmax.androidtodolist.dataaccess.TodoListRepository
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel: ViewModel() {

    private val todoListRepository: TodoListRepository by koinInject()
    private val timeServer: TimeServer by koinInject()

    val todoItems: MutableStateFlow<List<TodoListRepository.ToDoItem>> = MutableStateFlow(listOf())

    fun reload() {
        viewModelScope.launch(Dispatchers.IO) {
            todoItems.value = todoListRepository.loadToDoListFromMemory()
        }
    }

    fun clickToDoCheck(entity: TodoListRepository.ToDoItem) {
        todoItems.value = todoItems.value.map {
            if (it.uid == entity.uid) {
                val newCheckState = if (it.checkedAt == null) {
                    val localDateTime = timeServer.getCurrentLocalDateTime()
                    viewModelScope.launch(Dispatchers.IO) {
                        todoListRepository.setChecked(it.uid, localDateTime)
                        todoListRepository.triggerChange()
                    }
                    localDateTime
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        todoListRepository.setUnchecked(it.uid)
                        todoListRepository.triggerChange()
                    }
                    null
                }
                it.copy(checkedAt = newCheckState)
            } else {
                it
            }
        }
    }
}
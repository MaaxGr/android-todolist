package com.grossmax.androidtodolist.ui.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grossmax.androidtodolist.businesslogic.services.TimeServer
import com.grossmax.androidtodolist.dataaccess.TodoListRepository
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ViewItemScreenViewModel(savedStateHandle: SavedStateHandle): ViewModel() {

    // args
    private val todoId = checkNotNull(savedStateHandle.get<Int>("taskId"))

    // dependencies
    private val todoListRepository: TodoListRepository by koinInject()
    private val timeServer: TimeServer by koinInject()

    // state
    val taskItemState: MutableStateFlow<TodoListRepository.ToDoItem?> = MutableStateFlow(null)
    val loaded = MutableStateFlow(false)
    val taskTitle = MutableStateFlow("")
    val checkedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val somethingChanged = MutableStateFlow(false)

    init {
        Log.i("ViewItemScreenViewModel", "init with $todoId")

        loaded.value = false
        somethingChanged.value = false
        taskTitle.value = ""
        checkedState.value = false

        viewModelScope.launch(Dispatchers.IO) {
            val todoItem = todoListRepository.loadToDoListFromMemory().find { it.uid == todoId }
            taskItemState.value = todoItem
            taskTitle.value = todoItem?.title ?: ""
            checkedState.value = todoItem?.checkedAt != null
            loaded.value = true
        }
    }

    fun setTitle(title: String) {
        taskTitle.value = title
        somethingChanged.value = true

        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.setTitle(todoId, title)
        }
    }

    fun deleteToDo() {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.deleteById(todoId)
            todoListRepository.triggerChange()
        }
    }

    fun updateToDoChecked() {
        val newCheckState = if (!checkedState.value) {
            val localDateTime = timeServer.getCurrentLocalDateTime()
            viewModelScope.launch(Dispatchers.IO) {
                todoListRepository.setChecked(todoId, localDateTime)
                todoListRepository.triggerChange()
            }
            true
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                todoListRepository.setUnchecked(todoId)
                todoListRepository.triggerChange()
            }
            false
        }
        checkedState.value = newCheckState
    }

}
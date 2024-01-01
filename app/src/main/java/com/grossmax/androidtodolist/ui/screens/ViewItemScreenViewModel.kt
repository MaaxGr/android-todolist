package com.grossmax.androidtodolist.ui.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grossmax.androidtodolist.data.TodoListRepository
import com.grossmax.androidtodolist.data.database.dao.ToDoDao
import com.grossmax.androidtodolist.data.database.entity.ToDoEntity
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ViewItemScreenViewModel(savedStateHandle: SavedStateHandle): ViewModel() {

    private val todoId = checkNotNull(savedStateHandle.get<Int>("taskId"))

    private val todoListRepository: TodoListRepository by koinInject()
    private val todoListDao: ToDoDao by koinInject()

    val taskItemState: MutableStateFlow<ToDoEntity?> = MutableStateFlow(null)

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
            val todoItem = todoListDao.getAll().find { it.uid == todoId }
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
            todoListDao.setTitle(todoId, title)
        }
    }

    fun deleteToDo() {
        viewModelScope.launch(Dispatchers.IO) {
            todoListDao.delete(taskItemState.value!!)
            todoListRepository.triggerChange()
        }
    }

    fun updateToDoChecked() {
        val newCheckState = if (!checkedState.value) {
            val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            viewModelScope.launch(Dispatchers.IO) {
                todoListDao.setChecked(todoId, localDateTime)
                todoListRepository.triggerChange()
            }
            true
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                todoListDao.setUnchecked(todoId)
                todoListRepository.triggerChange()
            }
            false
        }
        checkedState.value = newCheckState
    }

}
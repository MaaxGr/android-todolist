package com.grossmax.androidtodolist.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grossmax.androidtodolist.data.TodoListRepository
import com.grossmax.androidtodolist.data.database.dao.ToDoDao
import com.grossmax.androidtodolist.data.database.entity.ToDoEntity
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeScreenViewModel: ViewModel() {

    private val todoListRepository: TodoListRepository by koinInject()
    private val todoDao: ToDoDao by koinInject()

    val testState: MutableStateFlow<List<ToDoEntity>> = MutableStateFlow(listOf())

    fun reload() {
        viewModelScope.launch(Dispatchers.IO) {
            testState.value = todoDao.getAll()
        }
    }

    fun clickToDoCheck(entity: ToDoEntity) {
        testState.value = testState.value.map {
            if (it.uid == entity.uid) {
                val newCheckState = if (it.checkedAt == null) {
                    val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    viewModelScope.launch(Dispatchers.IO) {
                        todoDao.setChecked(it.uid, localDateTime)
                        todoListRepository.triggerChange()
                    }
                    localDateTime
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        todoDao.setUnchecked(it.uid)
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
package com.grossmax.androidtodolist.ui.widgets

import android.util.Log
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

class WidgetViewModel: ViewModel() {

    private val todoDao: ToDoDao by koinInject()
    private val todoListRepository: TodoListRepository by koinInject()

    val testState: MutableStateFlow<List<ToDoEntity>> = MutableStateFlow(listOf())
    //var testState2 = listOf<String>()

    fun init() {

    }

    fun loadTodoList() {
        Log.i("WidgetViewModel", "loadTodoList")

        viewModelScope.launch(Dispatchers.IO) {
            val todoList = todoDao.getAll()
            //testState2 = todoList.filter { it.checkedAt == null }.map { it.title }

            testState.value = todoList.filter { it.checkedAt == null }
            Log.i("WidgetViewModel", "after")
        }
    }

    fun checkTask(toDoEntity: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.setChecked(toDoEntity.uid, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
            loadTodoList()
            todoListRepository.triggerChange()
        }
    }

    fun addTodoItem(text: String) {
        //testState.value = testState.value + text

        viewModelScope.launch(Dispatchers.IO) {
            todoDao.insertAll(
                ToDoEntity(
                    uid = 0,
                    title = text,
                    checkedAt = null,
                    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                )
            )
        }
    }

}
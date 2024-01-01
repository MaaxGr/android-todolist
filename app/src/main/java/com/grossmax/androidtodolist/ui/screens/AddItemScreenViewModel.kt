package com.grossmax.androidtodolist.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grossmax.androidtodolist.businesslogic.services.TimeServer
import com.grossmax.androidtodolist.dataaccess.TodoListRepository
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItemScreenViewModel: ViewModel() {

    private val todoListRepository: TodoListRepository by koinInject()
    private val timeServer: TimeServer by koinInject()

    val title = MutableStateFlow("")

    fun setTitle(title: String) {
        this.title.value = title
    }

    fun save(onSaved: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.addTodoItem(
                TodoListRepository.ToDoItem(
                    uid = 0,
                    title = title.value,
                    checkedAt = null,
                    createdAt = timeServer.getCurrentLocalDateTime()
                )
            )
            todoListRepository.triggerChange()
            withContext(Dispatchers.Main) {
                onSaved()
            }
        }
    }

}
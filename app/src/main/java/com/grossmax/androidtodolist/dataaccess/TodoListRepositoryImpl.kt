package com.grossmax.androidtodolist.dataaccess

import com.grossmax.androidtodolist.dataaccess.room.dao.ToDoDao
import com.grossmax.androidtodolist.dataaccess.room.entity.ToDoEntity
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.datetime.LocalDateTime

class TodoListRepositoryImpl: TodoListRepository {

    private val todoDao: ToDoDao by koinInject()
    private val listeners = mutableListOf<() -> Unit>()

    override fun loadToDoListFromMemory(): List<TodoListRepository.ToDoItem> {
        return todoDao.getAll().map {
            TodoListRepository.ToDoItem(
                uid = it.uid,
                title = it.title,
                checkedAt = it.checkedAt,
                createdAt = it.createdAt,
            )
        }
    }

    override fun addTodoItem(item: TodoListRepository.ToDoItem) {
        todoDao.insertAll(
            ToDoEntity(
                uid = item.uid,
                title = item.title,
                checkedAt = item.checkedAt,
                createdAt = item.createdAt,
            )
        )
    }

    override fun setChecked(id: Int, localDateTime: LocalDateTime) {
        todoDao.setChecked(id, localDateTime)
    }

    override fun setUnchecked(id: Int) {
        todoDao.setUnchecked(id)
    }

    override fun setTitle(id: Int, title: String) {
        todoDao.setTitle(id, title)
    }

    override fun deleteById(id: Int) {
        todoDao.deleteById(id)
    }

    override fun addChangeListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    override fun triggerChange() {
        listeners.forEach { it() }
    }

}
package com.grossmax.androidtodolist.data

class TodoListRepositoryImpl: TodoListRepository {

    private val todoList = mutableListOf<TodoListRepository.ToDoItem>()
    private val listeners = mutableListOf<() -> Unit>()

    override fun getToDoList(): List<TodoListRepository.ToDoItem> {
        return todoList
    }

    override fun addTodoItem(item: TodoListRepository.ToDoItem) {
        todoList.add(item)
    }

    override fun addChangeListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    override fun triggerChange() {
        listeners.forEach { it() }
    }

}
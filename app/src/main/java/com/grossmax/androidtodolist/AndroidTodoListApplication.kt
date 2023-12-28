package com.grossmax.androidtodolist

import android.app.Application
import androidx.room.Room
import com.grossmax.androidtodolist.data.TodoListRepository
import com.grossmax.androidtodolist.data.TodoListRepositoryImpl
import com.grossmax.androidtodolist.data.database.AppRoomDatabase
import com.grossmax.androidtodolist.ui.widgets.WidgetViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AndroidTodoListApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appRoomDatabase = Room.databaseBuilder(
            applicationContext,
            AppRoomDatabase::class.java,
            "todo-database"
        ).fallbackToDestructiveMigration().build()
        //TODO Remove this

        startKoin {
            modules(
                module {
                    single { WidgetViewModel() }
                    single<TodoListRepository> { TodoListRepositoryImpl() }
                    single { appRoomDatabase }
                    single { appRoomDatabase.todoDao() }
                }
            )
        }
    }

}
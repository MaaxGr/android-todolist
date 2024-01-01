package com.grossmax.androidtodolist

import android.app.Application
import androidx.room.Room
import com.grossmax.androidtodolist.businesslogic.services.TimeServer
import com.grossmax.androidtodolist.dataaccess.TodoListRepository
import com.grossmax.androidtodolist.dataaccess.TodoListRepositoryImpl
import com.grossmax.androidtodolist.dataaccess.room.AppRoomDatabase
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
                    // business logic
                    single { TimeServer() }

                    // ui
                    single { WidgetViewModel() }
                    single<TodoListRepository> { TodoListRepositoryImpl() }

                    // data access
                    single { appRoomDatabase }
                    single { appRoomDatabase.todoDao() }
                }
            )
        }
    }

}
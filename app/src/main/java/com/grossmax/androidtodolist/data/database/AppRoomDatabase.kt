package com.grossmax.androidtodolist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grossmax.androidtodolist.data.database.dao.ToDoDao
import com.grossmax.androidtodolist.data.database.entity.ToDoEntity

@TypeConverters(AppRoomConverters::class)
@Database(entities = [ToDoEntity::class], version = 1)
abstract class AppRoomDatabase: RoomDatabase() {

    abstract fun todoDao(): ToDoDao

}
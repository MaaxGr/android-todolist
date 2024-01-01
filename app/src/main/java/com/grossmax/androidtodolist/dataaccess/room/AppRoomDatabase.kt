package com.grossmax.androidtodolist.dataaccess.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grossmax.androidtodolist.dataaccess.room.dao.ToDoDao
import com.grossmax.androidtodolist.dataaccess.room.entity.ToDoEntity

@TypeConverters(AppRoomConverters::class)
@Database(entities = [ToDoEntity::class], version = 1)
abstract class AppRoomDatabase: RoomDatabase() {

    abstract fun todoDao(): ToDoDao

}
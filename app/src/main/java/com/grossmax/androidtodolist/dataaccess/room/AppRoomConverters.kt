package com.grossmax.androidtodolist.dataaccess.room

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.SimpleDateFormat
import java.util.Date

class AppRoomConverters {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())

    @TypeConverter
    fun fromStrongToKotlinLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun kotlinLocalDateTimeToString(date: LocalDateTime?): String? {
        return date?.let {
            val utilDate = Date(date.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
            dateFormat.format(utilDate)
        }
    }

}
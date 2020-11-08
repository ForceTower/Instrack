package dev.forcetower.instrack.core.source.local.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

object DateConverters {
    @JvmStatic
    @TypeConverter
    fun localDateTimeTimeToLong(date: LocalDateTime): Long {
        return date.toInstant(ZoneOffset.UTC).epochSecond
    }

    @TypeConverter
    @JvmStatic
    fun longToLocalDateTime(value: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.of("UTC"))
    }
}
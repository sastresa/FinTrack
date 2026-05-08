package com.fintrack.data.local.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth

class Converters {
    @TypeConverter
    fun localDateToEpochDay(value: LocalDate?): Long? = value?.toEpochDay()

    @TypeConverter
    fun epochDayToLocalDate(value: Long?): LocalDate? = value?.let(LocalDate::ofEpochDay)

    @TypeConverter
    fun instantToEpochMillis(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun epochMillisToInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun yearMonthToString(value: YearMonth?): String? = value?.toString()

    @TypeConverter
    fun stringToYearMonth(value: String?): YearMonth? = value?.let(YearMonth::parse)
}

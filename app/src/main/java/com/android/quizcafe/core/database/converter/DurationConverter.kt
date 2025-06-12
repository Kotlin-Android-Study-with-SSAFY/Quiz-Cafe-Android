package com.android.quizcafe.core.database.converter

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class DurationConverter {
    @TypeConverter
    fun fromDuration(duration: Duration): Long {
        return duration.inWholeMilliseconds
    }

    @TypeConverter
    fun toDuration(value: Long): Duration {
        return value.milliseconds
    }
}

package com.android.quizcafe.core.database.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId

class QuizIdConverter{

    @TypeConverter
    fun idToLong(id : QuizId): Long = id.value

    @TypeConverter
    fun longToId(value: Long) : QuizId = QuizId(value)

}

class QuizBookIdConverter{

    @TypeConverter
    fun idToLong(id : QuizBookId): Long = id.value

    @TypeConverter
    fun longToId(value: Long) : QuizBookId = QuizBookId(value)

}

class QuizBookGradeIdConverter{

    @TypeConverter
    fun localIdToLong(id : QuizBookGradeLocalId): Long = id.value

    @TypeConverter
    fun longToLocalId(value: Long) : QuizBookGradeLocalId = QuizBookGradeLocalId(value)

    @TypeConverter
    fun serverIdToLong(id : QuizBookGradeServerId): Long = id.value

    @TypeConverter
    fun longToServerId(value: Long) : QuizBookGradeServerId = QuizBookGradeServerId(value)
}

package com.android.quizcafe.core.database.dao.quiz

import androidx.room.Dao
import androidx.room.Upsert
import com.android.quizcafe.core.database.model.grading.QuizGradeEntity

@Dao
interface QuizGradeDao {

    @Upsert
    suspend fun upsert(entity: QuizGradeEntity): Long
}

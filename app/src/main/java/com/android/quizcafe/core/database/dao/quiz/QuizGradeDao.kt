package com.android.quizcafe.core.database.dao.quiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.quizcafe.core.database.model.grading.QuizGradeEntity

@Dao
interface QuizGradeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: QuizGradeEntity): Long

    @Query("SELECT * FROM QuizGradeEntity WHERE quizId = :quizId AND quizBookGradeLocalId = :quizBookGradeLocalId")
    suspend fun getQuizGradeByQuizId(quizId: Long, quizBookGradeLocalId: Long): QuizGradeEntity?
}

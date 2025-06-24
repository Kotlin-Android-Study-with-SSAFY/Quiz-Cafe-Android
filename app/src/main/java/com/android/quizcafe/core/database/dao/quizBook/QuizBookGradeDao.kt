package com.android.quizcafe.core.database.dao.quizBook

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.android.quizcafe.core.database.model.grading.QuizBookGradeEntity
import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizGradesRelation

@Dao
interface QuizBookGradeDao {

    @Upsert
    suspend fun upsertQuizBookGrade(entity: QuizBookGradeEntity): Long

    @Transaction
    @Query("SELECT * FROM QuizBookGradeEntity WHERE localId = :quizBookGradeLocalId")
    suspend fun getQuizBookGradeByLocalId(quizBookGradeLocalId: Long): QuizBookGradeWithQuizGradesRelation?

    @Transaction
    @Query("SELECT * FROM QuizBookGradeEntity WHERE quizBookId = :quizBookId")
    suspend fun getQuizBookGrade(quizBookId: Long): QuizBookGradeWithQuizGradesRelation?

    @Query("DELETE FROM QuizBookGradeEntity WHERE localId = :localId")
    suspend fun deleteQuizBookGrade(localId: Long): Int
}

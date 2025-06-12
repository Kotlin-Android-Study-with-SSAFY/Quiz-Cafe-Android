package com.android.quizcafe.core.database.dao.quizBook

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.android.quizcafe.core.database.model.grading.QuizBookGradeEntity
import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizGradesRelation

@Dao
interface QuizBookGradeDao{

    @Upsert
    suspend fun upsertQuizBookGrade(entity : QuizBookGradeEntity) : Long

    // QuizBookGradeEntity LocalId로 QuizQuizBookSolvingResult 리스트 반환
    @Transaction
    @Query("SELECT * FROM QuizBookGradeEntity WHERE localId = :localId")
    suspend fun getQuizBookGrade(localId : Long) : QuizBookGradeWithQuizGradesRelation?

    @Delete
    suspend fun deleteQuizBookGrade(localId : Long)
}

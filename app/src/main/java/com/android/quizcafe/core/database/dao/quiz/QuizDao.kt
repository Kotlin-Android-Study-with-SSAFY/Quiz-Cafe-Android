package com.android.quizcafe.core.database.dao.quiz

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.android.quizcafe.core.database.model.quiz.McqOptionEntity
import com.android.quizcafe.core.database.model.quiz.QuizEntity
import com.android.quizcafe.core.database.model.quiz.QuizWithMcqOptionsRelation

@Dao
interface QuizDao {

    @Upsert
    suspend fun upsertQuizList(quizList: List<QuizEntity>)

    @Upsert
    suspend fun upsertMcqOptions(mcqOptions: List<McqOptionEntity>): List<Long>

    @Transaction
    @Query("SELECT * FROM quiz WHERE id = :quizId")
    suspend fun getQuizWithOptionsById(quizId: Long): QuizWithMcqOptionsRelation?

    @Transaction
    @Query("SELECT * FROM quiz WHERE quizBookId = :quizBookId")
    suspend fun getQuizListWithOptionsByBookId(quizBookId: Long): List<QuizWithMcqOptionsRelation>

    @Query("SELECT * FROM mcq_option WHERE quizId = :quizId")
    suspend fun getMcqOptionsByQuizId(quizId: Long): List<McqOptionEntity>

}

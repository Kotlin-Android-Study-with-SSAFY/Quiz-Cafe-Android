package com.android.quizcafe.core.database.dao.quiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.quizcafe.core.database.model.quiz.QuizEntity
import com.android.quizcafe.core.database.model.quiz.QuizWithMcqOptionsRelation

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizList(quizList: List<QuizEntity>)
    @Transaction
    @Query("SELECT * FROM quiz WHERE id = :quizId")
    suspend fun getQuizWithOptionsById(quizId: Long): QuizWithMcqOptionsRelation?

    @Transaction
    @Query("SELECT * FROM quiz WHERE quizBookId = :quizBookId")
    suspend fun getQuizListWithOptionsByBookId(quizBookId: Long): List<QuizWithMcqOptionsRelation>

}

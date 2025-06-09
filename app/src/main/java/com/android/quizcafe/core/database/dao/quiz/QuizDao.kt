package com.android.quizcafe.core.database.dao.quiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.quizcafe.core.database.model.QuizEntity

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizList(quizList: List<QuizEntity>)

    @Query("SELECT * FROM quiz WHERE id = :quizId")
    suspend fun getQuizById(quizId: Long): QuizEntity

    @Query("SELECT * FROM quiz WHERE quizBookId = :quizBookId")
    suspend fun getQuizListByBookId(quizBookId: Long): List<QuizEntity>
}

package com.android.quizcafe.core.database.dao.quizBook

import androidx.room.Dao
import androidx.room.Query
import com.android.quizcafe.core.database.model.QuizBookEntity

@Dao
interface QuizBookDao {


    @Query("SELECT * FROM quiz_book WHERE category = :category")
    suspend fun getQuizBooksByCategory(category: String): List<QuizBookEntity>

    @Query("SELECT id FROM quiz_book WHERE category = :category")
    suspend fun getQuizBookIdsByCategory(category: String): List<Long>


}

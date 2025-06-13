package com.android.quizcafe.core.database.dao.quizBook

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.android.quizcafe.core.database.model.quizbook.QuizBookEntity
import com.android.quizcafe.core.database.model.quizbook.QuizBookWithQuizRelation

@Dao
interface QuizBookDao {

    @Upsert
    suspend fun upsertQuizBook(quizBook: QuizBookEntity): Long

    @Query("SELECT * FROM quiz_book WHERE id = :quizBookId")
    suspend fun getQuizBookById(quizBookId: Long): QuizBookEntity

    @Transaction
    @Query("SELECT * FROM quiz_book WHERE id = :quizBookId")
    suspend fun getQuizBookWithQuizList(quizBookId: Long): QuizBookWithQuizRelation?

    @Transaction
    @Query("SELECT * FROM quiz_book")
    suspend fun getAllQuizBooksWithQuizzes(): List<QuizBookWithQuizRelation>

    @Query("SELECT * FROM quiz_book WHERE category = :category")
    suspend fun getQuizBooksByCategory(category: String): List<QuizBookEntity>

    @Query("SELECT id FROM quiz_book WHERE category = :category")
    suspend fun getQuizBookIdsByCategory(category: String): List<Long>
}

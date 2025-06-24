package com.android.quizcafe.core.database.dao.quizBook

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.android.quizcafe.core.database.model.grading.QuizBookGradeEntity
import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizBookData
import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizGradesRelation

@Dao
interface QuizBookGradeDao {

    @Upsert
    suspend fun upsertQuizBookGrade(entity: QuizBookGradeEntity): Long

    // QuizBookGradeEntity LocalId로 QuizQuizBookSolvingResult 리스트 반환
    @Transaction
    @Query("SELECT * FROM QuizBookGradeEntity WHERE localId = :localId")
    suspend fun getQuizBookGrade(localId: Long): QuizBookGradeWithQuizGradesRelation?

    @Query(
        """
SELECT 
    QuizBookGradeEntity.localId,
    QuizBookGradeEntity.serverId,
    QuizBookGradeEntity.quizBookId,
    QuizBookGradeEntity.elapsedTime,

    quiz_book.id AS quiz_book_id,
    quiz_book.version AS quiz_book_version,
    quiz_book.category AS quiz_book_category,
    quiz_book.title AS quiz_book_title,
    quiz_book.description AS quiz_book_description,
    quiz_book.level AS quiz_book_level,
    quiz_book.createdBy AS quiz_book_createdBy,
    quiz_book.createdAt AS quiz_book_createdAt,
    quiz_book.totalQuizzes AS quiz_book_totalQuizzes

FROM QuizBookGradeEntity 
JOIN quiz_book ON QuizBookGradeEntity.quizBookId = quiz_book.id
"""
    )
    suspend fun getAllQuizBookGradeWithQuizBook(): List<QuizBookGradeWithQuizBookData>

    @Query("DELETE FROM QuizBookGradeEntity WHERE localId = :localId")
    suspend fun deleteQuizBookGrade(localId: Long)
}

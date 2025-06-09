package com.android.quizcafe.core.database.dao.quizBook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.quizcafe.core.database.model.grading.QuizBookGradeEntity
import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizGradesRelation
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId

@Dao
interface QuizBookGradeDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity : QuizBookGradeEntity) : QuizBookGradeLocalId

    // QuizBookGradeEntity LocalId로 QuizGradingResult 리스트 반환
    @Transaction
    @Query("SELECT * FROM QuizBookGradeEntity WHERE localId = :id")
    suspend fun getQuizBookGrade(id : QuizBookGradeLocalId) : QuizBookGradeWithQuizGradesRelation

}

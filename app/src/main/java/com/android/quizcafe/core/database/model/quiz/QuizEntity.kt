package com.android.quizcafe.core.database.model.quiz

import androidx.room.Entity
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId

@Entity(tableName = "quiz")
data class QuizEntity (
    val id : QuizId,
    val quizBookId: QuizBookId,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String?,
    val version: Long
)

package com.android.quizcafe.core.database.model.quiz

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId

@Entity(tableName = "quiz")
data class QuizEntity (
    @PrimaryKey
    val id : Long,
    val quizBookId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String?,
    val version: Long
)

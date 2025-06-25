package com.android.quizcafe.core.database.model.quiz

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class QuizEntity(
    @PrimaryKey
    val id: Long,
    val quizBookId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String?
)

package com.android.quizcafe.core.data.model.quizbook.response

import com.android.quizcafe.core.data.model.quiz.QuizResponseDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizBookWithQuizzesResponseDto(
    @SerialName("id") val quizBookId: Long,
    val version: Long,
    val category: String,
    val title: String,
    val description: String,
    val level: String,
    val createdBy: String,
    val createdAt: String,
    val totalQuizzes: Int,
    val quizzes: List<QuizResponseDto>
)

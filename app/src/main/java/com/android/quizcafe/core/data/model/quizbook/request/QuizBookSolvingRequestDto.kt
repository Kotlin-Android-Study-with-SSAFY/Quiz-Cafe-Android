package com.android.quizcafe.core.data.model.quizbook.request

import com.android.quizcafe.core.data.model.quiz.QuizSolvingRequestDto
import kotlinx.serialization.Serializable

@Serializable
data class QuizBookSolvingRequestDto(
    val quizBookId: Long,
    val version: Long,
    val totalQuizzes: Int,
    val correctCount: Int,
    val completedAt: String, // ISO8601
    val quizzes: List<QuizSolvingRequestDto>
)


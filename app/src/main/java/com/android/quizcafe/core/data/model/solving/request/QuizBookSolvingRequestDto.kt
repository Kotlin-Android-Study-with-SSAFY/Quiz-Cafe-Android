package com.android.quizcafe.core.data.model.solving.request

import kotlinx.serialization.Serializable

@Serializable
data class QuizBookSolvingRequestDto(
    val quizBookId: Long,
    val version: Long,
    val totalQuizzes: Int,
    val correctCount: Int,
    val quizzes: List<QuizSolvingRequestDto>
)

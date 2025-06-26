package com.android.quizcafe.core.data.model.solving.request

import kotlinx.serialization.Serializable

@Serializable
data class QuizBookSolvingRequestDto(
    val quizBookId: Long,
    val version: Long,
    val totalQuizzes: Int,
    val correctCount: Int,
    val solvingTime: Long,
    val quizzes: List<QuizSolvingRequestDto>
)

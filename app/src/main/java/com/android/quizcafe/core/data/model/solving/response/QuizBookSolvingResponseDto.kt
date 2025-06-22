package com.android.quizcafe.core.data.model.solving.response

import kotlinx.serialization.Serializable

@Serializable
data class QuizBookSolvingResponseDto(
    val id: Long,
    val userId: Long,
    val quizBookId: Long,
    val version: Long,
    val level: String,
    val category: String,
    val title: String,
    val description: String,
    val totalQuizzes: Int,
    val correctCount: Int,
    val completedAt: String,
    val solvingTime: Long,
    val quizzes: List<QuizSolvingResponseDto>
)

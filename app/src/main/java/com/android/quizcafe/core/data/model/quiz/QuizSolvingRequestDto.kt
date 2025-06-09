package com.android.quizcafe.core.data.model.quiz

import kotlinx.serialization.Serializable

@Serializable
data class QuizSolvingRequestDto(
    val quizId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String,
    val memo: String,
    val userAnswer: String,
    val isCorrect: Boolean,
    val completedAt: String, // ISO8601
    val mcqOptions: List<McpOptionSolvingRequestDto>
)

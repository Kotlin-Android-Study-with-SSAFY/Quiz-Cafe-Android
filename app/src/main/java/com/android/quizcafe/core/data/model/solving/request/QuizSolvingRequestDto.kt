package com.android.quizcafe.core.data.model.solving.request

import kotlinx.serialization.Serializable

@Serializable
data class QuizSolvingRequestDto(
    val quizId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String?,
    val memo: String?,
    val userAnswer: String?,
    val isCorrect: Boolean,
    val mcqOptions: List<McqOptionSolvingRequestDto>
)

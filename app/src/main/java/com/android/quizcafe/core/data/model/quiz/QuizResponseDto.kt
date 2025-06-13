package com.android.quizcafe.core.data.model.quiz

import kotlinx.serialization.Serializable

@Serializable
data class QuizResponseDto(
    val id: Long,
    val quizBookId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String?,
    val mcqOption: List<McqOptionDto> = emptyList()
)

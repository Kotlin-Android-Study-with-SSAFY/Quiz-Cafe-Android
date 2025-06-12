package com.android.quizcafe.core.data.model.quiz

import kotlinx.serialization.Serializable

@Serializable
data class QuizDto(
    val id: Long,
    val quizBookId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String?,
    val version: Long,
    val mcqOption: List<McqOptionDto>? = null // MCQ 타입일 때만 사용
)

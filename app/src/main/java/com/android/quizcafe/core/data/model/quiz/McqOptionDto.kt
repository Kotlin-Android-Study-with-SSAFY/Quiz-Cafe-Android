package com.android.quizcafe.core.data.model.quiz

import kotlinx.serialization.Serializable

@Serializable
data class McqOptionDto(
    val id: Long,
    val quizId: Long,
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

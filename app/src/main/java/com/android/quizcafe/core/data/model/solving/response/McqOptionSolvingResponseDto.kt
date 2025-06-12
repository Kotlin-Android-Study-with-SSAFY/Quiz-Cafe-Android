package com.android.quizcafe.core.data.model.solving.response

data class McqOptionSolvingResponseDto(
    val id: Long,
    val quizSolvingId: Long,
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

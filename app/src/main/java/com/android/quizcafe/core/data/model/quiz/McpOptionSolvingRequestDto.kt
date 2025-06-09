package com.android.quizcafe.core.data.model.quiz

import kotlinx.serialization.Serializable

@Serializable
data class McpOptionSolvingRequestDto(
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

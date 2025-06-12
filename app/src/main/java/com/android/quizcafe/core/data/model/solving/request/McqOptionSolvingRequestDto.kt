package com.android.quizcafe.core.data.model.solving.request

import kotlinx.serialization.Serializable

@Serializable
data class McqOptionSolvingRequestDto(
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

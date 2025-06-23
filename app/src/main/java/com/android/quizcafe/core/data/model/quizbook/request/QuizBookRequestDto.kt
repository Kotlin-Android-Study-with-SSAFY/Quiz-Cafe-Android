package com.android.quizcafe.core.data.model.quizbook.request

import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookRequest
import kotlinx.serialization.Serializable

@Serializable
data class QuizBookRequestDto(
    val category: String
)

fun QuizBookRequest.toDto() =
    QuizBookRequestDto(
        category = this.category
    )

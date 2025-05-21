package com.android.quizcafe.core.data.model.quizbook.request

import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookDetailRequest
import kotlinx.serialization.Serializable

@Serializable
data class QuizBookDetailRequestDto(
    val quizBookId: Int
)

fun QuizBookDetailRequest.toDto() =
    QuizBookDetailRequestDto(
        quizBookId = this.quizBookId
    )

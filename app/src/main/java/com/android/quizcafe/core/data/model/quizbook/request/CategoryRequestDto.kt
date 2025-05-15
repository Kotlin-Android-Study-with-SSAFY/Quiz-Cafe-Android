package com.android.quizcafe.core.data.model.quizbook.request

import com.android.quizcafe.core.domain.model.quizbook.request.CategoryRequest
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequestDto(
    val quizType: String
)

fun CategoryRequest.toDto() =
    CategoryRequestDto(
        quizType = this.quizType
    )

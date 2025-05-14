package com.android.quizcafe.core.data.model.quizbook.response

import com.android.quizcafe.core.domain.model.quizbook.reponse.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponseDto(
    @SerialName("category") val categoryId: String,
    @SerialName("name") val categoryName: String,
    val group: String
)

fun CategoryResponseDto.toDomain() = Category(
    id = categoryId,
    name = categoryName,
    group = group
)

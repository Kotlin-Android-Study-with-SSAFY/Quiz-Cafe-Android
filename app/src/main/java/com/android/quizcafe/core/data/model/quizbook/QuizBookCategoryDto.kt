package com.android.quizcafe.core.data.model.quizbook

import kotlinx.serialization.Serializable

@Serializable
data class QuizBookCategoryDto(
    val category : String,
    val name : String,
    val group : String
)

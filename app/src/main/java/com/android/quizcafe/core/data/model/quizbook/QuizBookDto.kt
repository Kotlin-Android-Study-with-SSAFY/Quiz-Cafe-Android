package com.android.quizcafe.core.data.model.quizbook

import kotlinx.serialization.Serializable

@Serializable
data class QuizBookDto(
    val id: Long,
    val version: Long,
    val category: String,
    val title: String,
    val description: String
)

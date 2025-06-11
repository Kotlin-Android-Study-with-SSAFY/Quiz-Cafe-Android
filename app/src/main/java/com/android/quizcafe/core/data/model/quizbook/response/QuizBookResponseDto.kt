package com.android.quizcafe.core.data.model.quizbook.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizBookResponseDto(
    @SerialName("id") val quizBookId: Long,
    val version: Long,
    val category: String,
    val title: String,
    val description: String,
    val level: String,
    val createdBy: String,
    val createdAt: String,
    val totalQuizzes: Int
)


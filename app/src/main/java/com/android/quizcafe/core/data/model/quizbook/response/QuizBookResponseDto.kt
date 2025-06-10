package com.android.quizcafe.core.data.model.quizbook.response

import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
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

fun QuizBookResponseDto.toDomain() = QuizBook(
    id = quizBookId,
    version = version,
    category = category,
    description = description,
    level = level,
    title = title,
    ownerName = createdBy,
    totalQuizzes = totalQuizzes,
    totalComments = 0,
    totalSaves = 0,
    createdAt = createdAt
)

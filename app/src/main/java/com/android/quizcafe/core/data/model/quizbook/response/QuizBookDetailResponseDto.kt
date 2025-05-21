package com.android.quizcafe.core.data.model.quizbook.response

import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.response.QuizSummary
import kotlinx.serialization.Serializable

@Serializable
data class QuizBookDetailResponseDto(
    val category: String,
    val createdBy: String,
    val description: String,
    val id: Long,
    val level: String,
    val quizzes: List<QuizSummaryDto>,
    val title: String,
    val version: Long
)

@Serializable
data class QuizSummaryDto(
    val quizContent: String,
    val quizId: Long,
    val quizType: String
)

fun QuizBookDetailResponseDto.toDomain() = QuizBookDetail(
    id = id,
    ownerId = 0,
    ownerName = "",
    category = category,
    title = title,
    description = description,
    difficulty = level,
    averageScore = 0.0,
    totalSaves = 0,
    quizSummaries = quizzes.map{ it.toDomain() },
    comments = emptyList(),
    createdAt = ""
)

fun QuizSummaryDto.toDomain() = QuizSummary(
    quizId =quizId,
    quizContent = quizContent,
    quizType = quizType
)

package com.android.quizcafe.core.data.model.quizbook.response

import com.android.quizcafe.core.domain.model.QuizType
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.response.QuizSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import toRelativeDate

@Serializable
data class QuizBookDetailResponseDto(
    val category: String,
    val createdBy: String,
    val description: String,
    val id: Long,
    val level: String,
    val quizzes: List<QuizSummaryDto>,
    val title: String,
    val version: Long,
    val averageCorrectCount: Double,
    val createdAt: String,
    val totalSaves: Int,
    val views: Int,
    @SerialName("isSaved") val isMarked: Boolean,
    val ownerId: Long
)

@Serializable
data class QuizSummaryDto(
    val quizContent: String,
    val quizId: Long,
    val quizType: String
)

fun QuizBookDetailResponseDto.toDomain() = QuizBookDetail(
    id = id,
    ownerId = ownerId,
    ownerName = createdBy,
    category = category,
    title = title,
    description = description,
    difficulty = level,
    averageScore = "$averageCorrectCount / ${quizzes.size}",
    totalSaves = totalSaves,
    level = level,
    views = views,
    quizSummaries = quizzes.map { it.toDomain() },
    comments = emptyList(),
    createdAt = createdAt.toRelativeDate(),
    isMarked = isMarked
)

fun QuizSummaryDto.toDomain() = QuizSummary(
    quizId = quizId,
    quizContent = quizContent,
    quizType = when (quizType) {
        "MCQ" -> QuizType.MCQ
        "OX" -> QuizType.OX
        "SHORT_ANSWER" -> QuizType.SHORT_ANSWER
        else -> QuizType.UNKNOWN
    }
)

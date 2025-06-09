package com.android.quizcafe.core.data.model.quizsolvingrecord.response

import androidx.compose.ui.util.fastMap
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.McqOption
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.Quiz
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord
import kotlinx.serialization.Serializable

@Serializable
data class QuizSolvingRecordResponseDto(
    val id: Long,
    val userId: Long,
    val quizBookId: Long,
    val version: Long,
    val level: String,
    val category: String,
    val title: String,
    val description: String,
    val totalQuizzes: Int,
    val correctCount: Int,
    val completedAt: String,
    val quizzes: List<QuizResponseDto>
)

@Serializable
data class QuizResponseDto(
    val id: Long,
    val quizBookSolvingId: Long,
    val quizId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String,
    val memo: String,
    val userAnswer: String,
    val isCorrect: Boolean,
    val completedAt: String,
    val mcqOptions: List<McqOptionResponseDto>
)

@Serializable
data class McqOptionResponseDto(
    val id: Long,
    val quizSolvingId: Long,
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

fun QuizSolvingRecordResponseDto.toDomain(): QuizSolvingRecord = QuizSolvingRecord(
    id = id,
    userId = userId,
    quizBookId = quizBookId,
    version = version,
    level = level,
    category = category,
    title = title,
    description = description,
    totalQuizzes = totalQuizzes,
    correctCount = correctCount,
    completedAt = completedAt,
    quizzes = quizzes.fastMap { it.toDomain() }
)

fun QuizResponseDto.toDomain(): Quiz = Quiz(
    id = id,
    quizBookSolvingId = quizBookSolvingId,
    quizId = quizId,
    questionType = questionType,
    content = content,
    answer = answer,
    explanation = explanation,
    memo = memo,
    userAnswer = userAnswer,
    isCorrect = isCorrect,
    completedAt = completedAt,
    mcqOptions = mcqOptions.fastMap { it.toDomain() }
)

fun McqOptionResponseDto.toDomain(): McqOption = McqOption(
    id = id,
    quizSolvingId = quizSolvingId,
    optionNumber = optionNumber,
    optionContent = optionContent,
    isCorrect = isCorrect
)

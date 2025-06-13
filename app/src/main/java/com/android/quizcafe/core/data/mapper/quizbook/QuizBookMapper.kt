package com.android.quizcafe.core.data.mapper.quizbook

import androidx.compose.ui.util.fastMap
import com.android.quizcafe.core.data.mapper.quiz.toDomain
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookWithQuizzesResponseDto
import com.android.quizcafe.core.database.model.quizbook.QuizBookEntity
import com.android.quizcafe.core.database.model.quizbook.QuizBookWithQuizRelation
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook

fun QuizBookEntity.toDomain() = QuizBook(
    id = id,
    version = version,
    category = category,
    title = title,
    description = description,
    level = level,
    ownerName = createdBy,
    totalQuizzes = totalQuizzes,
    totalComments = 0,
    totalSaves = 0,
    createdAt = createdAt
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

fun QuizBookWithQuizzesResponseDto.toDomain() = QuizBook(
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
    createdAt = createdAt,
    quizList = quizzes.fastMap { it.toDomain() }
)


fun QuizBookWithQuizRelation.toDomain() = QuizBook(
    id = quizBookEntity.id,
    version = quizBookEntity.version,
    category = quizBookEntity.category,
    title = quizBookEntity.title,
    description = quizBookEntity.description,
    level = quizBookEntity.level,
    ownerName = quizBookEntity.createdBy,
    totalQuizzes = quizBookEntity.totalQuizzes,
    totalComments = 0,
    totalSaves = 0,
    createdAt = quizBookEntity.createdAt,
    quizList = quizEntities.fastMap { it.toDomain() }
)

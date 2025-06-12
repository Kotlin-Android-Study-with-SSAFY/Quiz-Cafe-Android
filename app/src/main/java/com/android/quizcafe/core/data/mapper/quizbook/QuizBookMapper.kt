package com.android.quizcafe.core.data.mapper.quizbook

import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.database.model.QuizBookEntity
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

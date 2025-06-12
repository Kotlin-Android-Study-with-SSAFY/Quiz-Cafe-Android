package com.android.quizcafe.core.data.mapper.solving

import com.android.quizcafe.core.data.model.solving.response.QuizBookSolvingResponseDto
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.domain.model.value.QuizBookId


fun QuizBookSolvingResponseDto.toDomain(): QuizBookSolving = QuizBookSolving(
    id = id,
    userId = userId,
    quizBookId = QuizBookId(quizBookId),
    version = version,
    level = level,
    category = category,
    title = title,
    description = description,
    totalQuizzes = totalQuizzes,
    correctCount = correctCount,
    completedAt = completedAt,
    quizSolvings = quizzes.map { it.toDomain() }
)

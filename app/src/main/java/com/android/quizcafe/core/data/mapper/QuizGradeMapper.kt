package com.android.quizcafe.core.data.mapper

import com.android.quizcafe.core.database.model.grading.QuizGradeEntity
import com.android.quizcafe.core.domain.model.quiz.QuizGrade

fun QuizGradeEntity.toDomain() = QuizGrade(
    localId = localId,
    quizId = quizId,
    quizBookGradingLocalId = quizBookGradingLocalId,
    userAnswer = userAnswer,
    isCorrect = isCorrect,
    completedAt = completedAt
)

fun QuizGrade.toEntity() = QuizGradeEntity(
    localId = localId,
    quizId = quizId,
    quizBookGradingLocalId = quizBookGradingLocalId,
    userAnswer = userAnswer,
    isCorrect = isCorrect,
    completedAt = completedAt
)

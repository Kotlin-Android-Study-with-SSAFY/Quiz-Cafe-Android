package com.android.quizcafe.core.data.mapper.quiz

import com.android.quizcafe.core.database.model.grading.QuizGradeEntity
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizId

fun QuizGradeEntity.toDomain() = QuizGrade(
    localId = localId,
    quizId = QuizId(quizId),
    quizBookGradeLocalId = QuizBookGradeLocalId(quizBookGradeLocalId),
    userAnswer = userAnswer,
    isCorrect = isCorrect,
    completedAt = completedAt,
)


fun QuizGrade.toEntity() = QuizGradeEntity(
    localId = localId,
    quizId = quizId.value,
    quizBookGradeLocalId = quizBookGradeLocalId.value,
    userAnswer = userAnswer,
    memo = memo,
    isCorrect = isCorrect,
    completedAt = completedAt
)

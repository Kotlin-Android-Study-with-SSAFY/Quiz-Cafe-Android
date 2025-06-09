package com.android.quizcafe.core.data.mapper

import com.android.quizcafe.core.data.model.quiz.QuizDto
import com.android.quizcafe.core.database.model.QuizEntity
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId

fun QuizDto.toEntity() = QuizEntity(
    id = QuizId(id),
    quizBookId = QuizBookId(quizBookId),
    questionType = questionType,
    content = content,
    answer = answer,
    explanation = explanation,
    version = version
)


fun QuizEntity.toDomain() = Quiz(
    id = id,
    quizBookId = quizBookId,
    questionType = questionType,
    content = content,
    answer = answer,
    explanation = explanation,
    version = version
)

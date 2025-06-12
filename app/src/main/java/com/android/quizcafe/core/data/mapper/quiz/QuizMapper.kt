package com.android.quizcafe.core.data.mapper.quiz

import com.android.quizcafe.core.data.model.quiz.QuizDto
import com.android.quizcafe.core.database.model.quiz.QuizEntity
import com.android.quizcafe.core.database.model.quiz.QuizWithMcqOptionsRelation
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId

fun QuizDto.toEntity() = QuizEntity(
    id = id,
    quizBookId = quizBookId,
    questionType = questionType,
    content = content,
    answer = answer,
    explanation = explanation,
    version = version,
)

fun QuizWithMcqOptionsRelation.toDomain() = Quiz(
    id = QuizId(quizEntity.id),
    quizBookId = QuizBookId(quizEntity.quizBookId),
    questionType = quizEntity.questionType,
    content = quizEntity.content,
    answer = quizEntity.answer,
    explanation = quizEntity.explanation,
    version = quizEntity.version,
    mcqOption = if (mcqOptions.isNotEmpty()) {
        mcqOptions.toDomain()
    } else {
        null
    }
)

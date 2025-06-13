package com.android.quizcafe.core.data.mapper.quiz

import com.android.quizcafe.core.data.model.quiz.QuizResponseDto
import com.android.quizcafe.core.database.model.quiz.QuizEntity
import com.android.quizcafe.core.database.model.quiz.QuizWithMcqOptionsRelation
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId

fun QuizResponseDto.toEntity() = QuizEntity(
    id = id,
    quizBookId = quizBookId,
    questionType = questionType,
    content = content,
    answer = answer,
    explanation = explanation
)

fun QuizResponseDto.toDomain() = Quiz(
    id = QuizId(id),
    quizBookId = QuizBookId(quizBookId),
    questionType = questionType,
    content = content,
    answer = answer,
    explanation = explanation,
    mcqOption = mcqOption.toDomain()
)


fun QuizWithMcqOptionsRelation.toDomain() = Quiz(
    id = QuizId(quizEntity.id),
    quizBookId = QuizBookId(quizEntity.quizBookId),
    questionType = quizEntity.questionType,
    content = quizEntity.content,
    answer = quizEntity.answer,
    explanation = quizEntity.explanation,
    mcqOption = mcqOptions.toDomain()
)

fun List<QuizResponseDto>.toDomain() = map { it.toDomain() }

fun List<QuizWithMcqOptionsRelation>.toDomain() = map { it.toDomain() }

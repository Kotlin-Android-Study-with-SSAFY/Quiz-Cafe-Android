package com.android.quizcafe.core.data.mapper.quiz

import androidx.compose.ui.util.fastMap
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
    mcqOption = mcqOption.fastMap { it.toDomain() }
)

fun QuizWithMcqOptionsRelation.toDomain() = Quiz(
    id = QuizId(quizEntity.id),
    quizBookId = QuizBookId(quizEntity.quizBookId),
    questionType = quizEntity.questionType,
    content = quizEntity.content,
    answer = quizEntity.answer,
    explanation = quizEntity.explanation,
    mcqOption = mcqOptions.fastMap { it.toDomain() }
)

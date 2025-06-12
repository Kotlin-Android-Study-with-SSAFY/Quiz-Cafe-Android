package com.android.quizcafe.core.data.mapper.solving

import com.android.quizcafe.core.data.model.solving.response.QuizSolvingResponseDto
import com.android.quizcafe.core.domain.model.solving.QuizSolving

fun QuizSolvingResponseDto.toDomain() : QuizSolving = QuizSolving(
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
    mcqOptionSolvings = mcqOptions?.map { it.toDomain() } ?: emptyList()
)

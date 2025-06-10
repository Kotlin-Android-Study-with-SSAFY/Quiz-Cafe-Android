package com.android.quizcafe.core.data.mapper

import com.android.quizcafe.core.database.model.quiz.McqOptionEntity
import com.android.quizcafe.core.domain.model.quiz.McqOption
import com.android.quizcafe.core.domain.model.value.QuizId

fun McqOptionEntity.toDomain() = McqOption(
    id = id,
    quizId = QuizId(quizId),
    optionNumber = optionNumber,
    optionContent = optionContent,
    isCorrect = isCorrect
)

fun McqOption.toEntity() = McqOptionEntity(
    id = id,
    quizId = quizId.value,
    optionNumber = optionNumber,
    optionContent = optionContent,
    isCorrect = isCorrect
)

fun List<McqOptionEntity>.toDomain(): List<McqOption> = map { it.toDomain() }

fun List<McqOption>.toEntity(): List<McqOptionEntity> = map { it.toEntity() }

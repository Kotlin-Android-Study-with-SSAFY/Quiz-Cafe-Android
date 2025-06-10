package com.android.quizcafe.core.data.mapper

import com.android.quizcafe.core.data.model.quizbook.QuizBookDto
import com.android.quizcafe.core.database.model.QuizBookEntity
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook

fun QuizBookDto.toEntity() = QuizBookEntity(
    id = id,
    version = version,
    category = category,
    title = title,
    description = description
)

fun QuizBookEntity.toDomain() = QuizBook(
    id = id,
    version = version,
    category = category,
    title = title,
    description = description
)

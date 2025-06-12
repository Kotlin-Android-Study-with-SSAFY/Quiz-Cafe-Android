package com.android.quizcafe.core.data.mapper.solving

import com.android.quizcafe.core.data.model.solving.response.McqOptionSolvingResponseDto
import com.android.quizcafe.core.domain.model.solving.McqOptionSolving

fun McqOptionSolvingResponseDto.toDomain(): McqOptionSolving = McqOptionSolving(
    id = id,
    quizSolvingId = quizSolvingId,
    optionNumber = optionNumber,
    optionContent = optionContent,
    isCorrect = isCorrect
)

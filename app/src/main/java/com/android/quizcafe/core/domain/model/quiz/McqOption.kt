package com.android.quizcafe.core.domain.model.quiz

import com.android.quizcafe.core.domain.model.value.QuizId

data class McqOption(
    val id: Long,
    val quizId: QuizId,
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

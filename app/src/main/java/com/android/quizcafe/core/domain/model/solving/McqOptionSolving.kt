package com.android.quizcafe.core.domain.model.solving

data class McqOptionSolving(
    val id: Long,
    val quizSolvingId: Long,
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

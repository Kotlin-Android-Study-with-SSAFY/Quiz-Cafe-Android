package com.android.quizcafe.core.domain.model.quizsolvingrecord.response

data class McqOptionSolving(
    val id: Long,
    val quizSolvingId: Long,
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

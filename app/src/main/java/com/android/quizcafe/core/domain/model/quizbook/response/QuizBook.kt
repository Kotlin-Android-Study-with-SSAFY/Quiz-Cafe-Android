package com.android.quizcafe.core.domain.model.quizbook.response

data class QuizBook(
    val id: Long,
    val version: Long,
    val category: String,
    val title: String,
    val description: String
)

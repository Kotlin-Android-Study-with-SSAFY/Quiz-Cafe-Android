package com.android.quizcafe.core.domain.model.quizbook.response

data class QuizBook(
    val id: Long,
    val version: Long,
    val category: String,
    val description: String,
    val level: String,
    val title: String,
    val ownerName: String,
    val totalQuizzes: Int,
    val totalComments: Int,
    val totalSaves: Int,
    val createdAt: String
)

package com.android.quizcafe.core.domain.model.quizbook.response

import com.android.quizcafe.core.domain.model.quiz.Quiz

data class QuizBook(
    val id: Long,
    val version: Long,
    val category: String,
    val title: String,
    val description: String,
    val level: String,
    val ownerName: String,
    val totalQuizzes: Int,
    val totalComments: Int = 0,
    val totalSaves: Int,
    val createdAt: String,
    val quizList : List<Quiz> = emptyList()
)

package com.android.quizcafe.core.domain.model.solving

import com.android.quizcafe.core.domain.model.value.QuizBookId

data class QuizBookSolving(
    val id: Long,
    val userId: Long,
    val quizBookId: QuizBookId,
    val version: Long,
    val level: String,
    val category: String,
    val title: String,
    val description: String,
    val totalQuizzes: Int,
    val correctCount: Int,
    val completedAt: String,
    val quizSolvings: List<QuizSolving>
)

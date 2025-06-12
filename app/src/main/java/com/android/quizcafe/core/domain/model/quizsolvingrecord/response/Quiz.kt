package com.android.quizcafe.core.domain.model.quizsolvingrecord.response

data class Quiz(
    val id: Long,
    val quizBookSolvingId: Long,
    val quizId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String,
    val memo: String,
    val userAnswer: String,
    val isCorrect: Boolean,
    val completedAt: String,
    val mcqOptions: List<McqOption>
)

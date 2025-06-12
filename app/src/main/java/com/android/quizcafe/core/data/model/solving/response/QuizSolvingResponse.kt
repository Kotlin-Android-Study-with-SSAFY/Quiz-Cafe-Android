package com.android.quizcafe.core.data.model.solving.response

data class QuizSolvingResponse(
    val id: Long,
    val quizBookSolvingId: Long,
    val quizId: Long,
    val questionType: String, // MCQ, SHORT_ANSWER, OX
    val content: String,
    val answer: String,
    val explanation: String?,
    val memo: String?,
    val userAnswer: String?,
    val isCorrect: Boolean,
    val completedAt: String,
    val mcqOptions: List<McqOptionSolvingResponse>?
)

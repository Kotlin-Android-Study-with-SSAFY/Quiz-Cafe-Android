package com.android.quizcafe.core.domain.model.quiz

import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId

data class Quiz(
    val id: QuizId,
    val quizBookId: QuizBookId,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String?,
    val mcqOption: List<McqOption>? = null // MCQ 타입일 때만 사용
)

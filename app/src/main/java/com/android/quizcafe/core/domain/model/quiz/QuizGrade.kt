package com.android.quizcafe.core.domain.model.quiz

import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizId

data class QuizGrade(
    val localId : Long = 0,
    val quizId : QuizId,
    val quizBookGradeLocalId : QuizBookGradeLocalId,
    val userAnswer : String,
    val memo : String? = null,
    val isCorrect : Boolean = false,
    val completedAt : String
)

package com.android.quizcafe.core.domain.model.quiz

import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizId

data class QuizGrade(
    val localId : Long = -1,
    val quizId : QuizId,
    val quizBookGradingLocalId : QuizBookGradeLocalId,
    val userAnswer : String,
    val isCorrect : Boolean? = null,
    val completedAt : String
)

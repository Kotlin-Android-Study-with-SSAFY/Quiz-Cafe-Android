package com.android.quizcafe.core.domain.model.quizbook

import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import kotlin.time.Duration

data class QuizBookGrade(
    val quizGrades: List<QuizGrade> = listOf(),
    val elapsedTime: Duration = Duration.ZERO,
)

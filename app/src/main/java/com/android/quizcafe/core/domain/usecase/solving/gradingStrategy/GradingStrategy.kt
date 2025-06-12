package com.android.quizcafe.core.domain.usecase.solving.gradingStrategy

import com.android.quizcafe.core.domain.model.quiz.Quiz

interface GradingStrategy {
    fun grade(quiz : Quiz, userAnswer : String) : Boolean
}

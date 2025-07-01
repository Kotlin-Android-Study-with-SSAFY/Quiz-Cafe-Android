package com.android.quizcafe.core.domain.usecase.solving.gradingStrategy

import com.android.quizcafe.core.domain.model.quiz.Quiz

class OxGradingStrategy : GradingStrategy {
    override fun grade(quiz: Quiz, userAnswer: String): Boolean {
        return quiz.answer == userAnswer
    }
}

class McqGradingStrategy : GradingStrategy {
    override fun grade(quiz: Quiz, userAnswer: String): Boolean {
        return try {
            val correctIndex = quiz.answer.toInt() - 1
            val correctAnswer = quiz.mcqOption?.get(correctIndex)?.optionContent
            correctAnswer.equals(userAnswer, ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }
}

class ShortAnswerGradingStrategy : GradingStrategy {
    override fun grade(quiz: Quiz, userAnswer: String): Boolean {
        return quiz.answer.trim() == userAnswer.trim()
    }
}

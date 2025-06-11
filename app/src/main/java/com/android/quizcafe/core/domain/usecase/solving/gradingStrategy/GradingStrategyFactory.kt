package com.android.quizcafe.core.domain.usecase.solving.gradingStrategy

object GradingStrategyFactory {
    fun getGradingStrategy(questionType: String): GradingStrategy {
        return when (questionType) {
            "OX" -> OxGradingStrategy()
            "MCQ" -> McqGradingStrategy()
            "SHORT_ANSWER" -> ShortAnswerGradingStrategy()
            else -> throw IllegalArgumentException("Invalid question type: $questionType")
        }
    }
}

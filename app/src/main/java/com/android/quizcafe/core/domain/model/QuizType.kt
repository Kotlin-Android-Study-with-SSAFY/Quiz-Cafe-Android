package com.android.quizcafe.core.domain.model

enum class QuizType(val quizTypeName: String) {
    MCQ("객관식"),
    OX("O, X"),
    SHORT_ANSWER("주관식 단답형"),
    UNKNOWN("-")
}

package com.android.quizcafe.core.domain.model.solving

import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook

data class QuizBookGradeWithQuizBook(
    val quizBook: QuizBook,
    val quizBookGrade: QuizBookGrade
)

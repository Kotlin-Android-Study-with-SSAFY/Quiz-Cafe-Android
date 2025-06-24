package com.android.quizcafe.core.database.model.grading

import androidx.room.Embedded
import com.android.quizcafe.core.database.model.quizbook.QuizBookEntity

data class QuizBookGradeWithQuizBookData(
    @Embedded
    val grade: QuizBookGradeEntity,
    @Embedded("quiz_book_")
    val quizBook: QuizBookEntity
)

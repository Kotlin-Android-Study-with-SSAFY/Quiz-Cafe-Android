package com.android.quizcafe.core.data.mapper

import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizGradesRelation
import com.android.quizcafe.core.domain.model.quizbook.QuizBookGrade

fun QuizBookGradeWithQuizGradesRelation.toDomain() = QuizBookGrade(
    quizGrades = quizGradings.map { it.toDomain() },
    elapsedTime = quizBookGradeEntity.elapsedTime
)

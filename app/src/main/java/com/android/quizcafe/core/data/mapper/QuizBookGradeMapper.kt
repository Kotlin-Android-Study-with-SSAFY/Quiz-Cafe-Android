package com.android.quizcafe.core.data.mapper

import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizGradesRelation
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade

fun QuizBookGradeWithQuizGradesRelation.toDomain() = QuizBookGrade(
    localId = quizBookGradeEntity.localId,
    serverId = quizBookGradeEntity.serverId,
    quizBookId = quizBookGradeEntity.quizBookId,
    quizGrades = quizGradeEntities.map { it.toDomain() },
    elapsedTime = quizBookGradeEntity.elapsedTime
)

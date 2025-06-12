package com.android.quizcafe.core.data.mapper.quizbook

import com.android.quizcafe.core.data.mapper.quiz.toDomain
import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizGradesRelation
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId

fun QuizBookGradeWithQuizGradesRelation.toDomain() = QuizBookGrade(
    localId = QuizBookGradeLocalId(quizBookGradeEntity.localId),
    serverId = QuizBookGradeServerId(quizBookGradeEntity.serverId),
    quizBookId = QuizBookId(quizBookGradeEntity.quizBookId),
    quizGrades = quizGradeEntities.map { it.toDomain() },
    elapsedTime = quizBookGradeEntity.elapsedTime
)


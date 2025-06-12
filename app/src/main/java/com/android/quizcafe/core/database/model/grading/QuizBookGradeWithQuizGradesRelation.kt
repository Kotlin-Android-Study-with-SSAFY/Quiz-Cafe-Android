package com.android.quizcafe.core.database.model.grading

import androidx.room.Embedded
import androidx.room.Relation

data class QuizBookGradeWithQuizGradesRelation(
    @Embedded val quizBookGradeEntity: QuizBookGradeEntity,
    @Relation(
        parentColumn = "localId",
        entityColumn = "quizBookGradeLocalId"
    )
    val quizGradeEntities: List<QuizGradeEntity>
)

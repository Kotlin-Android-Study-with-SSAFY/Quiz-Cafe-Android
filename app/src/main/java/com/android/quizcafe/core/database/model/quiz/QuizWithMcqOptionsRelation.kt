package com.android.quizcafe.core.database.model.quiz

import androidx.room.Embedded
import androidx.room.Relation

data class QuizWithMcqOptionsRelation(
    @Embedded val quizEntity: QuizEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "quizId"
    )
    val mcqOptions: List<McqOptionEntity>
)

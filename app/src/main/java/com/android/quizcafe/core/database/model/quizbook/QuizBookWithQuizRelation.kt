package com.android.quizcafe.core.database.model.quizbook

import androidx.room.Embedded
import androidx.room.Relation
import com.android.quizcafe.core.database.model.quiz.QuizWithMcqOptionsRelation

data class QuizBookWithQuizRelation(
    @Embedded
    val quizBookEntity: QuizBookEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "quizBookId"
    )
    val quizEntities: List<QuizWithMcqOptionsRelation>
)

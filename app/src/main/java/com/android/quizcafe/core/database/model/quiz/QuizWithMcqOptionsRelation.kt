package com.android.quizcafe.core.database.model.quiz

import androidx.room.Embedded
import androidx.room.Relation
import com.android.quizcafe.core.data.mapper.quiz.toDomain
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId

data class QuizWithMcqOptionsRelation(
    @Embedded val quizEntity: QuizEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "quizId"
    )
    val mcqOptions: List<McqOptionEntity>
)

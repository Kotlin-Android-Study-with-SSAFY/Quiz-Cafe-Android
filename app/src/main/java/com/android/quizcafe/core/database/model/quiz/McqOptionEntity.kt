package com.android.quizcafe.core.database.model.quiz

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.android.quizcafe.core.database.model.quiz.QuizEntity
import com.android.quizcafe.core.domain.model.value.QuizId

@Entity(
    tableName = "mcq_option",
    foreignKeys = [
        ForeignKey(
            entity = QuizEntity::class,
            parentColumns = ["id"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(value = ["quizId"])]
)
data class McqOptionEntity(
    @PrimaryKey
    val id: Long,
    val quizId: Long,
    val optionNumber: Int,
    val optionContent: String,
    val isCorrect: Boolean
)

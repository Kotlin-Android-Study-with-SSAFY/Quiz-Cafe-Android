package com.android.quizcafe.core.database.model.grading

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = QuizBookGradeEntity::class,
            parentColumns = ["localId"],
            childColumns = ["quizBookGradingLocalId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
    // TODO : Index 설정
)
data class QuizGradeEntity(
    @PrimaryKey(autoGenerate = true)
    val localId : Long,
    val serverId : Long? = null,
    val quizId : Long,
    val quizBookGradingLocalId : Long,
    val userAnswer : String,
    val isCorrect : Boolean?,
    val completedAt : String
)

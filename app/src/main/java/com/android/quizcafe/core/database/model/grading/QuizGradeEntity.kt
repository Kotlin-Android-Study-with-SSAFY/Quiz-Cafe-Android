package com.android.quizcafe.core.database.model.grading

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizId

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = QuizBookGradeEntity::class,
            parentColumns = ["localId"],
            childColumns = ["quizBookGradeLocalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["quizBookGradeLocalId"])]
)
data class QuizGradeEntity(
    @PrimaryKey(autoGenerate = true)
    val localId : Long = 0,
    val serverId : Long? = null,
    val quizId : Long,
    val quizBookGradeLocalId : Long,
    val userAnswer : String,
    val memo : String?,
    val isCorrect : Boolean,
    val completedAt : String
)

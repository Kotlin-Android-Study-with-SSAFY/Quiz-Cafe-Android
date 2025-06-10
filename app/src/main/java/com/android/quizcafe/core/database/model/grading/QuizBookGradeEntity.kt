package com.android.quizcafe.core.database.model.grading

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

@Entity
data class QuizBookGradeEntity(
    @PrimaryKey(autoGenerate = true)
    val localId : Long = 0,
    val serverId : Long? = null,
    val quizBookId : Long,
    val elapsedTime : Duration = Duration.ZERO
)

/**
 * @param id
 * 퀴즈북 채점결과에는 퀴즈 채점결과 리스트가 들어가야한다
 * 점수를 반환해주는 함수가 필요하다
 */

package com.android.quizcafe.core.database.model.grading

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import kotlin.time.Duration

@Entity
data class QuizBookGradeEntity(
    @PrimaryKey(autoGenerate = true)
    val localId : QuizBookGradeLocalId = QuizBookGradeLocalId(-1),
    val serverId : QuizBookGradeServerId? = null,
    val quizBookId : QuizBookId,
    val elapsedTime : Duration = Duration.ZERO
)

/**
 * @param id
 * 퀴즈북 채점결과에는 퀴즈 채점결과 리스트가 들어가야한다
 * 점수를 반환해주는 함수가 필요하다
 */

package com.android.quizcafe.core.domain.model.solving

import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import kotlin.time.Duration

data class QuizBookGrade(
    val localId: QuizBookGradeLocalId,
    val serverId: QuizBookGradeServerId? = null,
    val quizBookId: QuizBookId,
    val quizGrades: List<QuizGrade> = listOf(),
    val elapsedTime: Duration = Duration.Companion.ZERO
)

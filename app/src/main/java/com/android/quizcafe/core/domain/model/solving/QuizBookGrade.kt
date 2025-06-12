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
) {
    /**
     * 정답 개수를 반환
     */
    fun getTotalScore(): Int {
        return quizGrades.count { it.isCorrect }
    }

    /**
     * 전체 문제 개수를 반환
     */
    fun getTotalQuestions(): Int {
        return quizGrades.size
    }

    /**
     * 경과 시간을 적절한 형식으로 반환
     * 1시간 미만: "MM:SS"
     * 1시간 이상: "HH:MM:SS"
     */
    fun getSolvingTimeFormatted(): String {
        val totalSeconds = elapsedTime.inWholeSeconds
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}

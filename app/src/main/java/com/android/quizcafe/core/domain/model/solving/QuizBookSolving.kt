package com.android.quizcafe.core.domain.model.solving

import com.android.quizcafe.core.domain.model.value.QuizBookId
import kotlin.time.Duration

data class QuizBookSolving(
    val id: Long,
    val userId: Long,
    val quizBookId: QuizBookId,
    val version: Long,
    val level: String,
    val category: String,
    val title: String,
    val description: String,
    val totalQuizzes: Int,
    val correctCount: Int,
    val completedAt: String,
    val quizSolvingList: List<QuizSolving>,
    val elapsedTime: Duration = Duration.ZERO
) {

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

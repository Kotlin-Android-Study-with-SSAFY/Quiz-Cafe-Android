package com.android.quizcafe.core.domain.model.solving

import com.android.quizcafe.core.domain.model.value.QuizBookId
import java.util.SortedMap
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

/**
 * completedAt 문자열(예: "2025-06-25T11:21:03.530Z")에서
 * YYYY-MM-DD 부분만 잘라내어 반환합니다.
 */
fun QuizSolving.getCompletedDate(): String =
    this.completedAt.take(10)

/**
 * 연속된 QuizSolving 시퀀스에서 일자별 풀이 횟수를 집계하여
 * 날짜(키)순으로 정렬된 맵을 반환합니다.
 */
fun Sequence<QuizSolving>.toDailyCounts(): SortedMap<String, Int> =
    this
        .filter { it.completedAt.isNotBlank() }
        .map { it.getCompletedDate() }
        .groupingBy { it }
        .eachCount()
        .toSortedMap()

package com.android.quizcafe.core.domain.model.solving

data class QuizSolving(
    val id: Long,
    val quizBookSolvingId: Long,
    val quizId: Long,
    val questionType: String,
    val content: String,
    val answer: String,
    val explanation: String?,
    val memo: String?,
    val userAnswer: String?,
    val isCorrect: Boolean,
    val completedAt: String,
    val mcqOptionSolvingList: List<McqOptionSolving> = emptyList()
)

fun QuizSolving.getCorrectAnswerText(): String {
    return when (questionType) {
        "MCQ" -> {
            val answerNumber = answer.toIntOrNull()
            answerNumber?.let { num ->
                mcqOptionSolvingList.firstOrNull { it.optionNumber == num }?.optionContent
            } ?: "정답을 찾을 수 없습니다."
        }
        else -> answer
    }
}

package com.android.quizcafe.core.domain.model.quizbook.response

// --- Data Classes ---
data class QuizBookDetail(
    val id: Long = 0,
    val ownerId: Long = 0,
    val ownerName: String = "",
    val category: String = "",
    val title: String = "",
    val description: String = "",
    val difficulty: String = "",
    val averageScore: Double = 0.0,
    val totalSaves: Int = 0,
    val quizSummaries: List<QuizSummary> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val createdAt: String = ""
)

data class QuizSummary(
    val quizId: Long,
    val quizContent: String,
    val quizType: String // 예시로 String, 필요 시 enum으로 대체
)

data class Comment(
    val commentId: Long,
    val userId: Long,
    val userName: String,
    val commentContent: String
)

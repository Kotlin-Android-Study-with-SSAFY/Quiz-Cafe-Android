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
    val averageScore: String = "",
    val totalSaves: Int = 0,
    val level: String = "",
    val views: Int = 0,
    val quizSummaries: List<QuizSummary> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val createdAt: String = "",
    val isSaved: Boolean = false
)

data class QuizSummary(
    val quizId: Long,
    val quizContent: String,
    val quizType: String
)

data class Comment(
    val commentId: Long,
    val userId: Long,
    val userName: String,
    val commentContent: String
)

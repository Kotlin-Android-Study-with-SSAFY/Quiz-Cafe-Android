package com.android.quizcafe.feature.quizbookpicker

// 임시 데이터 클래스
data class QuizBook(
    val id: Long,
    val ownerId: Long,
    val ownerName: String,
    val category: String,
    val title: String,
    val difficulty: String,
    val totalQuizzes: Int,
    val totalComments: Int,
    val totalSaves: Int,
    val createdAt: String
)

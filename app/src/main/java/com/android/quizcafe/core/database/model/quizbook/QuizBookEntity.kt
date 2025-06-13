package com.android.quizcafe.core.database.model.quizbook

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_book")
data class QuizBookEntity(
    @PrimaryKey
    val id: Long,
    val version: Long,
    val category: String,
    val title: String,
    val description: String,
    val level: String,
    val createdBy: String,
    val createdAt: String,
    val totalQuizzes: Int
)

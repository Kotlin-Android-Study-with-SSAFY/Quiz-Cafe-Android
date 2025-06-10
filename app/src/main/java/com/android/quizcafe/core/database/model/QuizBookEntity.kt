package com.android.quizcafe.core.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.android.quizcafe.core.domain.model.value.QuizBookId

@Entity(tableName = "quiz_book")
data class QuizBookEntity (
    @PrimaryKey
    val id : Long,
    val version: Long,
    val category: String,
    val title: String,
    val description: String
)

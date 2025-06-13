package com.android.quizcafe.main.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed interface BottomNav {
    @Serializable
    data object Quiz : BottomNav

    @Serializable
    data object MyPage : BottomNav

    @Serializable
    data object WorkBook : BottomNav
}

@Serializable object Home

@Serializable data class CategoryList(val quizType: String)

@Serializable data class QuizBookList(val category: String)

@Serializable data class QuizBookDetail(val quizBookId: Long)

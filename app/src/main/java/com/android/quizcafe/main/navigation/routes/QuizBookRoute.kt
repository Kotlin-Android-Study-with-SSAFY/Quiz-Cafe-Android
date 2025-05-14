package com.android.quizcafe.main.navigation.routes

import com.android.quizcafe.main.navigation.NavRoute

sealed interface QuizBookRoute : NavRoute {
    data object Graph : QuizBookRoute {
        override val route = "quiz-book"
    }

    data object CategoryList : QuizBookRoute {
        override val route = "${Graph.route}/category"
    }

    data object QuizBookList : QuizBookRoute {
        override val route = "${Graph.route}/quiz-book-list"
    }

    data object QuizBookDetail : QuizBookRoute {
        override val route = "${Graph.route}/quiz-book-detail"
    }

    companion object {
        val startDestination = CategoryList.route
    }
}

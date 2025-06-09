package com.android.quizcafe.main.navigation.routes

import com.android.quizcafe.main.navigation.NavRoute

sealed interface MainRoute : NavRoute {
    data object Graph : MainRoute {
        override val route = "main"
    }

    data object Quiz : MainRoute {
        override val route = "${Graph.route}/quiz"
    }

    data object MyPage : MainRoute {
        override val route = "${Graph.route}/mypage"
    }

    data object Workbook : MainRoute {
        override val route = "${Graph.route}/workbook"
    }

    data object CategoryList : MainRoute {
        override val route = "${Graph.route}/category"
    }

    data object QuizBookList : MainRoute {
        override val route = "${Graph.route}/quiz-book-list"
    }

    data object QuizBookDetail : MainRoute {
        override val route = "${Graph.route}/quiz-book-detail"
    }

    companion object {
        val startDestination = Quiz.route
    }
}

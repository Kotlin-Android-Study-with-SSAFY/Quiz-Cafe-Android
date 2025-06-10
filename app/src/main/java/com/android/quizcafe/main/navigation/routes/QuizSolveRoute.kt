package com.android.quizcafe.main.navigation.routes

import com.android.quizcafe.main.navigation.NavRoute

sealed interface QuizSolveRoute : NavRoute {
    data object Graph : MainRoute {
        override val route = "quizSolve"
    }

    data object QuizSolve : MainRoute {
        override val route = "${Graph.route}/quizSolve"
    }

    companion object {
        val startDestination = QuizSolve.route
    }
}

package com.android.quizcafe.main.navigation.routes

import com.android.quizcafe.main.navigation.NavRoute

sealed interface QuizSolveRoute : NavRoute {
    data object Graph : QuizSolveRoute {
        override val route = "quizSolve"
    }

    data object QuizSolve : QuizSolveRoute {
        override val route = "${Graph.route}/quizSolve"
    }

    data object QuizSolvingResult : QuizSolveRoute {
        override val route = "${Graph.route}/quizSolvingResult"
    }

    companion object {
        val startDestination = QuizSolve.route
    }
}

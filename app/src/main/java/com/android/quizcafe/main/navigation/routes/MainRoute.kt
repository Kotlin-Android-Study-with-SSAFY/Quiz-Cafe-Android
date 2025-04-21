package com.android.quizcafe.main.navigation.routes

import com.android.quizcafe.main.navigation.NavRoute

sealed interface MainRoute : NavRoute {
    object Graph    : MainRoute { override val route = "main" }
    object Quiz     : MainRoute { override val route = "main/quiz" }
    object MyPage   : MainRoute { override val route = "main/mypage" }
    object Workbook : MainRoute { override val route = "main/workbook" }

    companion object {
        val startDestination = Quiz.route
    }
}
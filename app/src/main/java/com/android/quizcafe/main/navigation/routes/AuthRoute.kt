// AuthRoute.kt
package com.android.quizcafe.main.navigation.routes

import com.android.quizcafe.main.navigation.NavRoute

sealed interface AuthRoute : NavRoute {
    data object Graph : AuthRoute {
        override val route = "auth"
    }
    data object Login : AuthRoute {
        override val route = "${Graph.route}/login"
    }
    data object Signup : AuthRoute {
        override val route = "${Graph.route}/signup"
    }
    companion object {
        // [수정] companion object의 시작점을 Login.route로 변경
        val startDestination = Graph.route // "auth/login"
    }
}

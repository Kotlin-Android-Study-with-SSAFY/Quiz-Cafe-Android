package com.android.quizcafe.main.navigation.routes

import com.android.quizcafe.main.navigation.NavRoute

sealed interface AuthRoute : NavRoute {
    object Graph    : MainRoute { override val route = "auth" }
    object Login  : AuthRoute { override val route = "auth/login" }
    object Signup : AuthRoute { override val route = "auth/signup" }

    companion object {
        val startDestination = Login.route
    }
}
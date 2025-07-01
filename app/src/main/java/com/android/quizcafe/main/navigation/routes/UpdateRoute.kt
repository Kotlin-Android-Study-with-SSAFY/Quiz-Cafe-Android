package com.android.quizcafe.main.navigation.routes

import com.android.quizcafe.main.navigation.NavRoute

sealed interface UpdateRoute : NavRoute {
    data object Graph : UpdateRoute {
        override val route = "update-user-info"
    }

    data object UpdatePasswords : UpdateRoute {
        override val route = "${Graph.route}/password"
    }

    data object UpdateNickname : UpdateRoute {
        override val route = "${Graph.route}/nickname"
    }

    companion object {
        val startDestination = UpdatePasswords.route
    }
}

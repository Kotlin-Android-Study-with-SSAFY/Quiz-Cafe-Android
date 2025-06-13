package com.android.quizcafe.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

fun <R : Any> NavController.navigateSingleTopTo(
    route: R,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    this.navigate(route) {
        launchSingleTop = true
        builder?.invoke(this)
    }
}

fun <R : Any> NavHostController.navigateAndClearBackStack(
    targetRoute: R,
    inclusive: Boolean = true
) {
    this.navigate(targetRoute) {
        popUpTo(this@navigateAndClearBackStack.graph.id) {
            this.inclusive = inclusive
        }
        launchSingleTop = true
    }
}

fun <R : Any> NavController.navigatePopUpToStartDestination(
    targetRoute: R
) {
    this.navigate(targetRoute) {
        popUpTo(this@navigatePopUpToStartDestination.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

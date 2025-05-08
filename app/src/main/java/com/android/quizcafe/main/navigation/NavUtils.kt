package com.android.quizcafe.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

// 연속 네비게이트 방지용
fun NavController.navigateSingleTopTo(
    route: String,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    this.navigate(route) {
        launchSingleTop = true
        //추가 옵션 지정
        builder?.invoke(this)
    }
}

// 백스택 초기화 네비게이트
fun NavHostController.navigateAndClearBackStack(
    targetRoute: String,
    popUpToRoute: String,
    inclusive: Boolean = true
) {
    this.navigate(targetRoute) {
        popUpTo(popUpToRoute) {
            this.inclusive = inclusive
        }
        launchSingleTop = true
    }
}

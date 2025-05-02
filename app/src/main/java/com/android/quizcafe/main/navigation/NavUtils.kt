package com.android.quizcafe.main.navigation

import androidx.navigation.NavController
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

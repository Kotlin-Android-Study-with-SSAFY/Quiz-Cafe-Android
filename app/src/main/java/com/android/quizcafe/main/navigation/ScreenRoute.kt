package com.android.quizcafe.main.navigation;

sealed class Screen(val route: String) {
    object Auth    : Screen("auth")
    object Login   : Screen("login")
    object Signup  : Screen("signup")
    object Main     : Screen("main")
    object Quiz     : Screen("main/quiz")
    object MyPage   : Screen("main/mypage")
    object Workbook : Screen("main/workbook")
}
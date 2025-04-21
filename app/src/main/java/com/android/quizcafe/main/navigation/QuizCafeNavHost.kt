package com.android.quizcafe.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.android.quizcafe.feature.login.LoginScreen
import com.android.quizcafe.main.ui.QuizCafeApp


@Composable
fun QuizCafeNavHost(
    startDestination: String = Screen.Auth.route
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)
        mainGraph(navController)
    }
}

// 1 로그인/회원가입 그래프
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Login.route,
        route = Screen.Auth.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen()
        }
        composable(Screen.Signup.route) {
            //SignupScreen()
        }
    }
}

// 2 메인 탭
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Quiz.route,
        route = Screen.Main.route
    ) {
        composable(Screen.Quiz.route) {
           // QuizScreen()
        }
        composable(Screen.MyPage.route) {
           // MyPageScreen()
        }
        composable(Screen.Workbook.route) {
           // WorkbookScreen()
        }
    }
}
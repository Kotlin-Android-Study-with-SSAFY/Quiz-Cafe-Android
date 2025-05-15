package com.android.quizcafe.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.android.quizcafe.feature.categorylist.CategoryRoute
import com.android.quizcafe.feature.quizbooklist.QuizBookListRoute
import com.android.quizcafe.feature.signup.SignUpRoute
import com.android.quizcafe.main.navigation.routes.AuthRoute
import com.android.quizcafe.main.navigation.routes.MainRoute

@Composable
fun QuizCafeNavHost(
    startDestination: String = AuthRoute.startDestination
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthRoute.Graph.route
    ) {
        authGraph(navController)
        mainGraph(navController)
    }
}

// 1. 로그인 및 회원가입
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = AuthRoute.startDestination,
        route = AuthRoute.Graph.route
    ) {
        composable(AuthRoute.Login.route) {
//            LoginRoute(
//                navigateToSignUp = { navController.navigateSingleTopTo(AuthRoute.Signup.route) },
//                navigateToHome = {
//                    navController.navigateAndClearBackStack(MainRoute.Graph.route, AuthRoute.Login.route)
//                }
//            )
            CategoryRoute(
                navigateToHome = {
                    navController.navigate(MainRoute.Graph.route)
                },
                navigateToQuizBookList = {
                    navController.navigateSingleTopTo(MainRoute.QuizBookList.route)
                },
            )
        }
        composable(AuthRoute.Signup.route) {
            SignUpRoute(
                navigateToLogin = {
                    navController.navigateAndClearBackStack(AuthRoute.Login.route, AuthRoute.Signup.route)
                }
            )
        }
    }
}

// 메인 탭
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = MainRoute.startDestination,
        route = MainRoute.Graph.route
    ) {
        composable(MainRoute.Quiz.route) {
            //QuizScreen()
        }
        composable(MainRoute.MyPage.route) {
            //MyPageScreen()
        }
        composable(MainRoute.Workbook.route) {
            //WorkbookScreen()
        }
        composable(MainRoute.CategoryList.route) {
            CategoryRoute(
                navigateToHome = {
                    navController.navigate(MainRoute.Graph.route)
                },
                navigateToQuizBookList = {
                    navController.navigateSingleTopTo(MainRoute.QuizBookList.route)
                },
            )
        }

        composable(MainRoute.QuizBookList.route) {
            QuizBookListRoute(
                navigateToQuizBookDetail = { },
                navigateToCategory = { }
            )
        }
        composable(MainRoute.QuizBookDetail.route) {
//
        }
    }
}

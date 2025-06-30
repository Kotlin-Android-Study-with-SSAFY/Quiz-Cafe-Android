package com.android.quizcafe.main.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.android.quizcafe.feature.login.LoginRoute
import com.android.quizcafe.feature.main.MainScreen
import com.android.quizcafe.feature.quiz.solve.QuizSolveRoute
import com.android.quizcafe.feature.quiz.solvingResult.QuizBookSolvingResultRoute
import com.android.quizcafe.feature.quizbookdetail.QuizBookDetailRoute
import com.android.quizcafe.feature.signup.SignUpRoute
import com.android.quizcafe.main.navigation.routes.AuthRoute
import com.android.quizcafe.main.navigation.routes.MainRoute
import com.android.quizcafe.main.navigation.routes.QuizSolveRoute

@Composable
fun QuizCafeNavHost(
    navController: NavHostController,
    startDestination: String = AuthRoute.Graph.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
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
            LoginRoute(
                navigateToSignUp = { navController.navigateSingleTopTo(AuthRoute.Signup.route) },
                navigateToHome = {
                    navController.navigateAndClearBackStack(MainRoute.Graph.route)
                }
            )
        }
        composable(AuthRoute.Signup.route) {
            SignUpRoute(
                navigateToLogin = {
                    navController.navigateAndClearBackStack(AuthRoute.Login.route)
                },
                navigateToBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

// 메인 탭
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = MainRoute.Home.route,
        route = MainRoute.Graph.route
    ) {
        composable(MainRoute.Home.route) { MainScreen(rootNavController = navController) }
        composable(
            route = "${MainRoute.QuizBookDetail.route}/{quizBookId}",
            arguments = listOf(
                navArgument("quizBookId") {
                    type = NavType.LongType
                    nullable = false
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val quizBookId = backStackEntry.arguments?.getLong("quizBookId") ?: 0L

            QuizBookDetailRoute(
                quizBookId = quizBookId,
                navigateToQuizBookPicker = {},
                navigateToQuizSolve = { id -> navController.navigateSingleTopTo("${QuizSolveRoute.QuizSolve.route}/$id") },
                navigateToUserPage = {}
            )
        }
        quizSolveGraph(navController)
    }
}

// 퀴즈 풀이
fun NavGraphBuilder.quizSolveGraph(navController: NavHostController) {
    navigation(
        startDestination = QuizSolveRoute.QuizSolve.route,
        route = QuizSolveRoute.Graph.route
    ) {
        composable(
            route = "quizSolve/quizSolve/{quizBookId}",
            arguments = listOf(
                navArgument("quizBookId") {
                    type = NavType.LongType
                    nullable = false
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val quizBookId = backStackEntry.arguments?.getLong("quizBookId") ?: 0L
            QuizSolveRoute(
                quizBookId,
                navigateToBack = {
                    navController.popBackStack()
                },
                navigateToQuizBookSolvingResult = { quizBookGradeServerId ->
                    navController.navigate(
                        "${QuizSolveRoute.QuizSolvingResult.route}/${quizBookGradeServerId.value}"
                    )
                }
            )
        }
        composable(
            route = "${QuizSolveRoute.QuizSolvingResult.route}/{quizBookGradeServerId}",
            arguments = listOf(
                navArgument("quizBookGradeServerId") {
                    type = NavType.LongType
                    nullable = false
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val quizBookGradeServerId = backStackEntry.arguments?.getLong("quizBookGradeServerId") ?: 6L
            QuizBookSolvingResultRoute(
                quizBookGradeServerId = quizBookGradeServerId,
                navigateToMain = {
                    navController.navigateAndClearBackStack(MainRoute.Graph.route)
                }
            )
        }
    }
}

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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.android.quizcafe.feature.categorypicker.CategoryRoute
import com.android.quizcafe.feature.login.LoginRoute
import com.android.quizcafe.feature.main.MainScreen
import com.android.quizcafe.feature.main.mypage.MyPageRoute
import com.android.quizcafe.feature.main.quiz.QuizRoute
import com.android.quizcafe.feature.main.workbook.WorkBookRoute
import com.android.quizcafe.feature.quiz.solve.QuizSolveRoute
import com.android.quizcafe.feature.quiz.solvingResult.QuizBookSolvingResultRoute
import com.android.quizcafe.feature.quizbookdetail.QuizBookDetailRoute
import com.android.quizcafe.feature.quizbooklist.QuizBookListRoute
import com.android.quizcafe.feature.signup.SignUpRoute
import com.android.quizcafe.main.navigation.routes.AuthRoute
import com.android.quizcafe.main.navigation.routes.MainRoute
import com.android.quizcafe.main.navigation.routes.QuizSolveRoute

@Composable
fun QuizCafeNavHost(
    startDestination: String = AuthRoute.Graph.route
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = QuizSolveRoute.Graph.route,
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        authGraph(navController)
        mainGraph(navController)
        quizSolveGraph(navController)
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
                    navController.navigateAndClearBackStack(MainRoute.Graph.route, AuthRoute.Login.route)
                }
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
        startDestination = MainRoute.Quiz.route,
        route = MainRoute.Graph.route
    ) {
        composable(MainRoute.Quiz.route) {
            MainScreen()
        }
    }
}

@Composable
fun MainBottomNavHost(
    navController: NavHostController,
    startDestination: String = MainRoute.Quiz.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainRoute.Quiz.route) {
            QuizRoute(
                navigateToCategory = { _ -> navController.navigateSingleTopTo(MainRoute.CategoryList.route) }
            )
        }
        composable(MainRoute.Workbook.route) {
            WorkBookRoute(
//                onItemClick = { id ->
//                    navController.navigateSingleTopTo("")
//                }
            )
        }
        composable(MainRoute.MyPage.route) {
            MyPageRoute(
//                navigateToSetting = {
//                    navController.navigateSingleTopTo()
//                }
            )
        }
        composable(MainRoute.CategoryList.route) {
            CategoryRoute(
                navigateToQuizBookList = { category -> navController.navigateSingleTopTo("${MainRoute.QuizBookList.route}/$category") },
                navigateToHome = { navController.navigateUp() },
            )
        }
        composable(
            route = "${MainRoute.QuizBookList.route}/{category}",
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""

            QuizBookListRoute(
                category = category,
                navigateToQuizBookDetail = { quizBookId -> navController.navigateSingleTopTo("${MainRoute.QuizBookDetail.route}/$quizBookId") },
                navigateToCategory = {},
            )
        }
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
                navigateToQuizSolve = {},
                navigateToUserPage = {}
            )
        }
    }
}

// 퀴즈 풀이
fun NavGraphBuilder.quizSolveGraph(navController: NavHostController) {
    navigation(
        startDestination = QuizSolveRoute.QuizSolve.route,
        route = QuizSolveRoute.Graph.route
    ) {
        composable(QuizSolveRoute.QuizSolve.route) {
            QuizSolveRoute(
                navigateToBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${QuizSolveRoute.QuizSolvingResult.route}/{quizBookGradeLocalId}",
            arguments = listOf(
                navArgument("quizBookGradeLocalId") {
                    type = NavType.LongType
                    nullable = false
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val quizBookGradeLocalId = backStackEntry.arguments?.getLong("quizBookGradeLocalId") ?: 0L
            QuizBookSolvingResultRoute(
                quizBookGradeLocalId = quizBookGradeLocalId,
                navigateToMain = {
                    navController.navigateAndClearBackStack(MainRoute.Graph.route, QuizSolveRoute.QuizSolve.route)
                }
            )
        }
    }
}

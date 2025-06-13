package com.android.quizcafe.main.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.quizcafe.feature.categorypicker.CategoryRoute
import com.android.quizcafe.feature.login.LoginRoute
import com.android.quizcafe.feature.main.MainScreen
import com.android.quizcafe.feature.main.home.HomeRoute
import com.android.quizcafe.feature.main.mypage.MyPageRoute
import com.android.quizcafe.feature.main.workbook.WorkBookRoute
import com.android.quizcafe.feature.quiz.solve.QuizSolveRoute
import com.android.quizcafe.feature.quizbookdetail.QuizBookDetailRoute
import com.android.quizcafe.feature.quizbooklist.QuizBookListRoute
import com.android.quizcafe.feature.signup.SignUpRoute
import com.android.quizcafe.main.navigation.routes.Auth
import com.android.quizcafe.main.navigation.routes.CategoryList
import com.android.quizcafe.main.navigation.routes.Home
import com.android.quizcafe.main.navigation.routes.Login
import com.android.quizcafe.main.navigation.routes.BottomNav
import com.android.quizcafe.main.navigation.routes.QuizBookDetail
import com.android.quizcafe.main.navigation.routes.QuizBookList
import com.android.quizcafe.main.navigation.routes.Signup
import com.android.quizcafe.main.navigation.routes.Solve

@Composable
fun QuizCafeNavHost(
    navController: NavHostController,
    startDestination: Any = Auth
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
    navigation<Auth>(
        startDestination = Login,
    ) {
        composable<Login> {
            LoginRoute(
                navigateToSignUp = { navController.navigateSingleTopTo(Signup) },
                navigateToHome = {
                    navController.navigateAndClearBackStack(Home)
                }
            )
        }
        composable<Signup> {
            SignUpRoute(
                navigateToLogin = {
                    navController.navigateAndClearBackStack(Login)
                }
            )
        }
    }
}

// 메인 탭
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable<Home> { MainScreen(navController) }
    composable<Solve> { backStackEntry ->
        val quizBookId: Long = backStackEntry.toRoute<Solve>().quizBookId

        QuizSolveRoute(
            navigateToBack = {
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun MainBottomNavHost(
    bottomNavController: NavHostController,
    navController: NavHostController,
    startDestination: Any = BottomNav.Quiz
) {
    NavHost(
        navController = bottomNavController,
        startDestination = startDestination
    ) {
        composable<BottomNav.Quiz> {
            HomeRoute(
                navigateToCategory = { quizType -> bottomNavController.navigateSingleTopTo(CategoryList(quizType)) }
            )
        }
        composable<BottomNav.WorkBook> {
            WorkBookRoute(
//                onItemClick = { id ->
//                    bottomNavController.navigateSingleTopTo("")
//                }
            )
        }
        composable<BottomNav.MyPage> {
            MyPageRoute(
//                navigateToSetting = {
//                    bottomNavController.navigateSingleTopTo()
//                }
            )
        }
        composable<CategoryList> { backStackEntry ->
            val quizType: String = backStackEntry.toRoute<CategoryList>().quizType

            CategoryRoute(
                navigateToQuizBookList = { category -> bottomNavController.navigateSingleTopTo(QuizBookList(category)) },
                navigateToHome = { bottomNavController.navigateUp() },
            )
        }
        composable<QuizBookList> { backStackEntry ->
            val category: String = backStackEntry.toRoute<QuizBookList>().category

            QuizBookListRoute(
                category = category,
                navigateToQuizBookDetail = { quizBookId -> bottomNavController.navigateSingleTopTo(QuizBookDetail(quizBookId)) },
                navigateToCategory = { bottomNavController.navigateUp() },
            )
        }
        composable<QuizBookDetail> { backStackEntry ->
            val quizBookId: Long = backStackEntry.toRoute<QuizBookDetail>().quizBookId

            QuizBookDetailRoute(
                quizBookId = quizBookId,
                navigateToQuizBookPicker = { bottomNavController.navigateUp() },
                navigateToQuizSolve = { navController.navigateSingleTopTo(Solve(quizBookId)) },
                navigateToUserPage = {}
            )
        }
    }
}

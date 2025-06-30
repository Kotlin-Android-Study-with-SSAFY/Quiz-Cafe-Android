package com.android.quizcafe.feature.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.quizcafe.R
import com.android.quizcafe.feature.categorypicker.CategoryRoute
import com.android.quizcafe.feature.main.home.HomeRoute
import com.android.quizcafe.feature.main.mypage.MyPageRoute
import com.android.quizcafe.feature.main.workbook.WorkBookRoute
import com.android.quizcafe.feature.quizbookdetail.QuizBookDetailRoute
import com.android.quizcafe.feature.quizbooklist.QuizBookListRoute
import com.android.quizcafe.main.navigation.navigatePopUpToStartDestination
import com.android.quizcafe.main.navigation.navigateSingleTopTo
import com.android.quizcafe.main.navigation.routes.MainRoute
import com.android.quizcafe.main.navigation.routes.QuizSolveRoute

data class MainTab(
    val route: String,
    @StringRes val labelResId: Int
)

val mainTabs = listOf(
    MainTab(MainRoute.Home.route, R.string.tab_title_quiz),
    MainTab(MainRoute.Workbook.route, R.string.tab_title_workbook),
    MainTab(MainRoute.MyPage.route, R.string.tab_title_mypage)
)

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: MainRoute.Home.route

    val selectedIndex = mainTabs.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    val targetRoute = mainTabs[index].route
                    if (currentRoute != targetRoute) {
                        bottomNavController.navigatePopUpToStartDestination(targetRoute)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            MainBottomNavHost(rootNavController, bottomNavController)
        }
    }
}

@Composable
fun MainBottomNavHost(
    rootNavController: NavHostController,
    bottomNavController: NavHostController,
    startDestination: String = MainRoute.startDestination,
) {
    NavHost(
        navController = bottomNavController,
        startDestination = startDestination
    ) {
        composable(MainRoute.Home.route) {
            HomeRoute(
                navigateToCategory = { _ -> bottomNavController.navigateSingleTopTo(MainRoute.CategoryList.route) }
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
                navigateToQuizBookList = { category -> bottomNavController.navigateSingleTopTo("${MainRoute.QuizBookList.route}/$category") },
                navigateToHome = { bottomNavController.navigateUp() },
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
                navigateToQuizBookDetail = { quizBookId ->
                    bottomNavController.navigateSingleTopTo("${MainRoute.QuizBookDetail.route}/${quizBookId.value}")
                },
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
                navigateToQuizSolve = { id -> rootNavController.navigateSingleTopTo("${QuizSolveRoute.QuizSolve.route}/$id") },
                navigateToUserPage = {}
            )
        }
    }
}

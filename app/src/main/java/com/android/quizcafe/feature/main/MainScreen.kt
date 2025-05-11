package com.android.quizcafe.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.quizcafe.core.ui.QuizCafeTopAppBar
import com.android.quizcafe.core.ui.TopAppBarTitle
import com.android.quizcafe.feature.main.mypage.MyPageScreen
import com.android.quizcafe.feature.main.quiz.QuizRoute
import com.android.quizcafe.feature.main.workbook.WorkbookScreen
import com.android.quizcafe.main.navigation.navigateSingleTopTo
import com.android.quizcafe.main.navigation.routes.MainRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    startDestination: String = MainRoute.Quiz.route
) {
    val navController = rememberNavController()
    val tabRoutes = listOf(MainRoute.Quiz, MainRoute.Workbook, MainRoute.MyPage)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: startDestination
    val selectedIndex = tabRoutes.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    Scaffold (
        topBar = {
            QuizCafeTopAppBar(
                title = TopAppBarTitle.Text("Quiz Cafe"),
                alignTitleToStart = true
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    val route = tabRoutes[index].route
                    if (route != currentRoute) {
                        navController.navigateSingleTopTo(route)
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainRoute.Quiz.route) { QuizRoute() }
            composable(MainRoute.Workbook.route) { WorkbookScreen() }
            composable(MainRoute.MyPage.route) { MyPageScreen() }
        }
    }
}

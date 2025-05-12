package com.android.quizcafe.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.quizcafe.core.ui.QuizCafeTopAppBar
import com.android.quizcafe.core.ui.TopAppBarTitle
import com.android.quizcafe.feature.home.mypage.MyPageScreen
import com.android.quizcafe.feature.home.quiz.QuizRoute
import com.android.quizcafe.feature.home.workbook.WorkbookScreen
import com.android.quizcafe.main.navigation.navigateSingleTopTo
import com.android.quizcafe.main.navigation.routes.MainRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    val tabRoutes = listOf(MainRoute.Quiz, MainRoute.Workbook, MainRoute.MyPage)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: MainRoute.Quiz.route
    val selectedIndex = tabRoutes.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    Scaffold(
        topBar = {
            QuizCafeTopAppBar(
                title = TopAppBarTitle.Text("Quiz Cafe"),
                alignTitleToStart = true
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    val targetRoute = tabRoutes[index].route
                    if (currentRoute != targetRoute) {
                        navController.navigateSingleTopTo(targetRoute)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)){
            NavGraphBuilder.mainGraph(navController)
        }
    }
}

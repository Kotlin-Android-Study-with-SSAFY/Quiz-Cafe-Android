package com.android.quizcafe.feature.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.quizcafe.core.ui.QuizCafeTopAppBar
import com.android.quizcafe.core.ui.TopAppBarTitle
import com.android.quizcafe.main.navigation.routes.MainRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    val tabRoutes = listOf(MainRoute.Quiz, MainRoute.Workbook, MainRoute.MyPage)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: MainRoute.Quiz.route
    val selectedIndex = tabRoutes.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    val appBarTitle = when (currentRoute) {
        MainRoute.Quiz.route -> "Quiz Cafe"
        MainRoute.Workbook.route -> "저장한 문제집"
        MainRoute.MyPage.route -> ""
        else -> ""
    }

    Scaffold(
        topBar = {
            QuizCafeTopAppBar(
                title = TopAppBarTitle.Text(appBarTitle),
                alignTitleToStart = true
            )
        },
        bottomBar = {

        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = MainRoute.Graph.route
        ) {

        }
    }
}

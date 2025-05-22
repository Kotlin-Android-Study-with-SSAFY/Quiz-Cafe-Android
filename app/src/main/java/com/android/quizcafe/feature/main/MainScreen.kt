package com.android.quizcafe.feature.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.quizcafe.R
import com.android.quizcafe.main.navigation.MainBottomNavHost
import com.android.quizcafe.main.navigation.navigatePopUpToStartDestination
import com.android.quizcafe.main.navigation.routes.MainRoute

data class MainTab(
    val route: String,
    @StringRes val labelResId: Int
)

val mainTabs = listOf(
    MainTab(MainRoute.Quiz.route, R.string.tab_title_quiz),
    MainTab(MainRoute.Workbook.route, R.string.tab_title_workbook),
    MainTab(MainRoute.MyPage.route, R.string.tab_title_mypage)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: MainRoute.Quiz.route

    val selectedIndex = mainTabs.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    val targetRoute = mainTabs[index].route
                    if (currentRoute != targetRoute) {
                        navController.navigatePopUpToStartDestination(targetRoute)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            MainBottomNavHost(navController)
        }
    }
}

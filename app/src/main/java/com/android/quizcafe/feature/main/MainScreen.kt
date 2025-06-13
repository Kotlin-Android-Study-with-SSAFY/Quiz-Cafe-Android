package com.android.quizcafe.feature.main

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.android.quizcafe.R
import com.android.quizcafe.main.navigation.MainBottomNavHost
import com.android.quizcafe.main.navigation.navigatePopUpToStartDestination
import com.android.quizcafe.main.navigation.routes.*

data class BottomNavTab(
    val route: Any,
    @StringRes val labelResId: Int
)

val mainTabs = listOf(
    BottomNavTab(BottomNav.Quiz, R.string.tab_title_quiz),
    BottomNavTab(BottomNav.WorkBook, R.string.tab_title_workbook),
    BottomNavTab(BottomNav.MyPage, R.string.tab_title_mypage)
)

@Composable
fun MainScreen(navController: NavHostController) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryFlow.collectAsStateWithLifecycle(null)
    var navBarState = when (navBackStackEntry?.destination?.route) {
        BottomNav.Quiz::class.simpleName -> 0
        BottomNav.WorkBook::class.simpleName -> 1
        BottomNav.MyPage::class.simpleName -> 2
        else -> -1
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = navBarState,
                onItemSelected = { index ->
                    val targetRoute = mainTabs[index].route
                    if (navBarState != targetRoute) {
                        Log.d("navtest", "$navBarState / $targetRoute")
                        bottomNavController.navigatePopUpToStartDestination(targetRoute)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            MainBottomNavHost(bottomNavController, navController)
        }
    }
}

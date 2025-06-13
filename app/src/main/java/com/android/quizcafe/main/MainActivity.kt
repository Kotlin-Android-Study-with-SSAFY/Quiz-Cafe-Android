package com.android.quizcafe.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.android.quizcafe.main.navigation.QuizCafeNavHost
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.main.navigation.routes.AuthRoute
import com.android.quizcafe.main.navigation.routes.MainRoute
import com.android.quizcafe.core.datastore.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QuizCafeTheme {
                val navController = rememberNavController()
                var startDestination: String? by remember { mutableStateOf(null) }

                LaunchedEffect(Unit) {
                    val accessToken = authManager.getToken()
                    startDestination = if (accessToken != null) MainRoute.Graph.route else AuthRoute.Graph.route
                }
                startDestination?.let {
                    QuizCafeNavHost(navController, startDestination = it)
                }

                AppEventsHandler(
                    authManager = authManager,
                    navController = navController,
                    loginRoute = AuthRoute.Graph.route
                )
            }
        }
    }
}

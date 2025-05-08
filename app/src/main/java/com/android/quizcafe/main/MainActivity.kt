package com.android.quizcafe.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.main.navigation.QuizCafeNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizCafeTheme{
                QuizCafeNavHost()
            }
        }
    }
}

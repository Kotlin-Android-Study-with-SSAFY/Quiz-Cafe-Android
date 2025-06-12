package com.android.quizcafe.feature.main.mypage

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import android.widget.Toast

@Composable
fun MyPageRoute(
    viewModel: MyPageViewModel = hiltViewModel(),
    onNavigateToAlarm: () -> Unit = {},
    onNavigateToChangePw: () -> Unit = {},
    onNavigateToMyQuizSet: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sendIntent(MyPageIntent.LoadUserInfo)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MyPageEffect.NavigateToAlarm -> onNavigateToAlarm()
                is MyPageEffect.NavigateToChangePw -> onNavigateToChangePw()
                is MyPageEffect.NavigateToMyQuizSet -> onNavigateToMyQuizSet()
                is MyPageEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    MyPageScreen(
        state = state,
        onClick = { intent -> viewModel.sendIntent(intent) }
    )
}

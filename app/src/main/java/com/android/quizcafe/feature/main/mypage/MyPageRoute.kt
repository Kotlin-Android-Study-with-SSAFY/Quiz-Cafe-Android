package com.android.quizcafe.feature.main.mypage

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import android.widget.Toast

@Composable
fun MyPageRoute(
    viewModel: MyPageViewModel = hiltViewModel(),
    onNavigateToStats: () -> Unit = {},
    onNavigateToAlarm: () -> Unit = {},
    onNavigateToChangePw: () -> Unit = {},
    onNavigateToMyQuizSet: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // 최초 진입 시 유저 정보 불러오기
    LaunchedEffect(Unit) {
        viewModel.sendIntent(MyPageIntent.LoadUserInfo)
    }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MyPageEffect.NavigateToStats -> onNavigateToStats()
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
        onClickStats = { viewModel.sendIntent(MyPageIntent.ClickStats(state.solvedCount)) },
        onClickAlarm = { viewModel.sendIntent(MyPageIntent.ClickAlarm) },
        onClickChangePw = { viewModel.sendIntent(MyPageIntent.ClickChangePw) },
        onClickMyQuizSet = { viewModel.sendIntent(MyPageIntent.ClickMyQuizSet) }
    )
}

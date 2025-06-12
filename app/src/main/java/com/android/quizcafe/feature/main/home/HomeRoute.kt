package com.android.quizcafe.feature.main.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    navigateToCategory: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sendIntent(HomeIntent.FetchRecord)

        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.ShowErrorDialog -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is HomeEffect.NavigateToCategory -> {
                    navigateToCategory(effect.quizType)
                }
            }
        }
    }

    HomeScreen(
        state = state,
        sendIntent = viewModel::sendIntent
    )
}

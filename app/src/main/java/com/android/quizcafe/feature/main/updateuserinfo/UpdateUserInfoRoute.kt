package com.android.quizcafe.feature.main.updateuserinfo

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UpdateUserInfoRoute(
    step: Int,
    onNavigateBack: () -> Unit,
    viewModel: UpdateUserInfoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is UpdateUserInfoEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is UpdateUserInfoEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    UpdateUserInfoScreen(
        step = step,
        state = state,
        intent = viewModel::sendIntent,
        onNavigateBack = onNavigateBack
    )
}

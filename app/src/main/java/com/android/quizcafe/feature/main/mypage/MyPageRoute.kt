package com.android.quizcafe.feature.main.mypage

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import android.widget.Toast
import com.android.quizcafe.R

@Composable
fun MyPageRoute(
    viewModel: MyPageViewModel = hiltViewModel(),
    onNavigateToChangeUserInfo: () -> Unit = {},
    onNavigateToMyCreatedQuizBooks: () -> Unit = {},
    onNavigateToLogout: () -> Unit = {},
    onNavigateToWithdrawal: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showWithdrawalFirstDialog by remember { mutableStateOf(false) }
    var showWithdrawalFinalDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(MyPageIntent.LoadUserInfo)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MyPageEffect.NavigateToChangeUserInfo -> onNavigateToChangeUserInfo()
                is MyPageEffect.NavigateToMyCreatedQuizBooks -> onNavigateToMyCreatedQuizBooks()
                is MyPageEffect.NavigateToLogout -> onNavigateToLogout()
                is MyPageEffect.NavigateToWithdrawal -> onNavigateToWithdrawal()
                is MyPageEffect.ShowLogoutDialog -> showLogoutDialog = true
                is MyPageEffect.ShowWithdrawalFirstDialog -> showWithdrawalFirstDialog = true
                is MyPageEffect.ShowWithdrawalFinalDialog -> showWithdrawalFinalDialog = true
                is MyPageEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            message = R.string.dialog_message_logout,
            onConfirm = {
                viewModel.sendIntent(MyPageIntent.ConfirmLogout)
                showLogoutDialog = false
            },
            onCancel = {
                viewModel.sendIntent(MyPageIntent.CancelLogout)
                showLogoutDialog = false
            }
        )
    }

    if (showWithdrawalFirstDialog) {
        ConfirmDialog(
            message = R.string.dialog_message_withdrawal_first,
            onConfirm = {
                viewModel.sendIntent(MyPageIntent.ConfirmWithdrawalFirst)
                showWithdrawalFirstDialog = false
            },
            onCancel = {
                viewModel.sendIntent(MyPageIntent.CancelWithdrawalFirst)
                showWithdrawalFirstDialog = false
            }
        )
    }

    if (showWithdrawalFinalDialog) {
        ConfirmDialog(
            message = R.string.dialog_message_withdrawal_final,
            onConfirm = {
                viewModel.sendIntent(MyPageIntent.ConfirmWithdrawalFinal)
                showWithdrawalFinalDialog = false
            },
            onCancel = {
                viewModel.sendIntent(MyPageIntent.CancelWithdrawalFinal)
                showWithdrawalFinalDialog = false
            }
        )
    }

    MyPageScreen(
        state = state,
        onClick = { intent -> viewModel.sendIntent(intent) }
    )
}

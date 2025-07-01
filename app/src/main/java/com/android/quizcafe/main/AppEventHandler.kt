package com.android.quizcafe.main

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.android.quizcafe.R
import com.android.quizcafe.core.datastore.AuthManager
import com.android.quizcafe.core.datastore.LogoutReason
import com.android.quizcafe.main.navigation.navigateAndClearBackStack
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppEventsHandler(
    authManager: AuthManager,
    navController: NavHostController,
    loginRoute: String
) {
    val context = LocalContext.current

    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var dialogMessage by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authManager.logoutEvent.collectLatest { reason ->
            when (reason) {
                is LogoutReason.SessionExpired -> {
                    dialogMessage = reason.message
                    showLogoutDialog = true
                }

                LogoutReason.UserLogout -> {
                    dialogMessage = context.getString(R.string.dialog_message_user_logout)
                    showLogoutDialog = true
                }

                LogoutReason.UserWithdrawal -> {
                    dialogMessage = context.getString(R.string.dialog_message_user_withdrawal)
                    showLogoutDialog = true
                }

                LogoutReason.PasswordUpdated -> {
                    dialogMessage = context.getString(R.string.dialog_message_password_updated)
                    showLogoutDialog = true
                }
            }
        }
    }

    if (showLogoutDialog) {
        navController.navigateAndClearBackStack(loginRoute)
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = stringResource(R.string.notification)) },
            text = { Text(text = dialogMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                }) {
                    Text(text = stringResource(R.string.confirm))
                }
            }
        )
    }
}

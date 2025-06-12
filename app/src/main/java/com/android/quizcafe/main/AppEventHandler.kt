package com.android.quizcafe.main

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var showLogoutDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authManager.logoutEvent.collectLatest { reason ->
            when (reason) {
                is LogoutReason.SessionExpired -> {
                    dialogMessage = reason.message
                    showLogoutDialog = true
                }

                LogoutReason.UserLogout -> TODO()
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = stringResource(R.string.error)) },
            text = { Text(text = dialogMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    navController.navigateAndClearBackStack(loginRoute)
                }) {
                    Text(stringResource(R.string.confirm))
                }
            }
        )
    }
}

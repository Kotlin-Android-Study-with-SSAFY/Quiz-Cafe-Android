package com.android.quizcafe.feature.signup

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R

@Composable
fun SignUpRoute(
    navigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var step by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SignUpEffect.NavigateToPasswordInput -> {
                    step = 1
                    Toast.makeText(
                        context,
                        context.getString(R.string.success_email_verification),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignUpEffect.ShowErrorDialog -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignUpEffect.NavigateToLoginScreen -> {
                    navigateToLogin()
                    Toast.makeText(
                        context,
                        context.getString(R.string.success_signup),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    val state by viewModel.state.collectAsState()

    SignUpScreen(
        step = step,
        state = state,
        sendIntent = viewModel::sendIntent
    )
}

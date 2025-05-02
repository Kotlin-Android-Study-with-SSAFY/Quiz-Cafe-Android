package com.android.quizcafe.feature.signup

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R
import com.android.quizcafe.core.ui.QuizCafeTopAppBar
import com.android.quizcafe.core.ui.TopAppBarTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var step by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SignUpEffect.NavigateToPasswordInput -> {
                    step = 1
                    Toast.makeText(context, context.getString(R.string.success_email_verification), Toast.LENGTH_SHORT).show()
                }
                is SignUpEffect.ShowErrorDialog -> {
                    Toast.makeText(context, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show()
                }
                is SignUpEffect.NavigateToLoginScreen -> {
                    navigateToLogin()
                    Toast.makeText(context, context.getString(R.string.success_signup), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        topBar = {
            QuizCafeTopAppBar(
                title = TopAppBarTitle.Text(stringResource(R.string.signup)),
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.navigate_up))
                    }
                }
            )
        },
        bottomBar = {
            BottomActionButton(
                state = state,
                onClick = {
                    when {
                        state.isSuccessVerification -> viewModel.onIntent(SignUpIntent.ClickSignUp)
                        state.isCodeSent -> viewModel.onIntent(SignUpIntent.ClickVerifyCode)
                        else -> viewModel.onIntent(SignUpIntent.ClickSendCode)
                    }
                }
            )
        }
    ) { innerPadding ->
        AnimatedContent(step) {
            when (it) {
                0 -> EmailInputContent(viewModel, state, innerPadding)
                1 -> PasswordInputContent(viewModel, state, innerPadding)
            }
        }
    }
}

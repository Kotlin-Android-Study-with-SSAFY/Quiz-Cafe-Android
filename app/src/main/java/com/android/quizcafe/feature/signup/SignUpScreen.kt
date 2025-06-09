package com.android.quizcafe.feature.signup

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.QuizCafeTopAppBar
import com.android.quizcafe.core.ui.TopAppBarTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    step: Int,
    state: SignUpViewState,
    sendIntent: (SignUpIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        topBar = {
            QuizCafeTopAppBar(
                title = TopAppBarTitle.Text(stringResource(R.string.signup)),
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomActionButton(
                state = state,
                onClick = {
                    when {
                        state.isSuccessVerification -> sendIntent(SignUpIntent.ClickSignUp)
                        state.isCodeSent -> sendIntent(SignUpIntent.ClickVerifyCode)
                        else -> sendIntent(SignUpIntent.ClickSendCode)
                    }
                }
            )
        }
    ) { innerPadding ->
        AnimatedContent(step) {
            when (it) {
                0 -> EmailInputContent(state, sendIntent, innerPadding)
                1 -> PasswordInputContent(state, sendIntent, innerPadding)
            }
        }
    }
}

@Composable
fun BottomActionButton(state: SignUpViewState, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        QuizCafeButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = if (state.isSuccessVerification) state.isSignUpEnabled else state.isNextEnabled
        ) {
            Text(stringResource(R.string.next))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpEmailVerificationPreview() {
    QuizCafeTheme {
        SignUpScreen(0, SignUpViewState()) {}
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPasswordPreview() {
    QuizCafeTheme {
        SignUpScreen(1, SignUpViewState()) {}
    }
}

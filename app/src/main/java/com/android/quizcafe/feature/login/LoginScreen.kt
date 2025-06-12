package com.android.quizcafe.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

@Composable
fun LoginScreen(
    state: LoginUiState,
    sendIntent: (LoginIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            QuizCafeLogo()
        }
        item {
            EmailInputContent(state, sendIntent)
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            PasswordInputContent(state, sendIntent)
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            LoginButton(
                onClick = {
                    sendIntent(LoginIntent.ClickLogin)
                },
                state = state
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            BottomTextOptions(sendIntent)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    QuizCafeTheme {
        LoginScreen(LoginUiState()) {}
    }
}

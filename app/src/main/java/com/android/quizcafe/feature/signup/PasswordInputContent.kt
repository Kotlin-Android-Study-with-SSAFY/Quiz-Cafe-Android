package com.android.quizcafe.feature.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.LabeledInputField
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.AnimatedTitleWithBody
import kotlinx.coroutines.delay

@Composable
fun PasswordInputContent(
    state: SignUpUiState,
    sendIntent: (SignUpIntent) -> Unit,
    innerPadding: PaddingValues
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    AnimatedTitleWithBody(
        title = stringResource(R.string.input_password),
        innerPadding = innerPadding,
        content = {
            PasswordInputContent(state, focusRequester, sendIntent)
            Spacer(modifier = Modifier.height(32.dp))
            PasswordConfirmInputContent(state, sendIntent)
        }
    )
}

@Composable
fun PasswordInputContent(
    state: SignUpUiState,
    focusRequester: FocusRequester?,
    sendIntent: (SignUpIntent) -> Unit
) {
    LabeledInputField(
        label = stringResource(R.string.password),
        value = state.password,
        onValueChange = { sendIntent(SignUpIntent.UpdatedPassword(it)) },
        isPassword = true,
        focusRequester = focusRequester,
        errorMessage = state.passwordErrorMessage
    )
}

@Composable
fun PasswordConfirmInputContent(state: SignUpUiState, sendIntent: (SignUpIntent) -> Unit) {
    LabeledInputField(
        label = stringResource(R.string.password_confirm),
        value = state.passwordConfirm,
        onValueChange = { sendIntent(SignUpIntent.UpdatedPasswordConfirm(it)) },
        isPassword = true,
        errorMessage = state.passwordConfirmErrorMessage
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordInputPreview() {
    QuizCafeTheme {
        Column {
            PasswordInputContent(SignUpUiState(), null) {}
            Spacer(Modifier.height(20.dp))
            PasswordInputContent(SignUpUiState(password = "password"), null) {}
            Spacer(Modifier.height(20.dp))
            PasswordInputContent(
                SignUpUiState(
                    password = "password",
                    passwordErrorMessage = "비밀번호 형식이 올바르지 않습니다."
                ),
                null
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordConfirmInputPreview() {
    QuizCafeTheme {
        Column {
            PasswordConfirmInputContent(SignUpUiState()) {}
            Spacer(Modifier.height(20.dp))
            PasswordConfirmInputContent(SignUpUiState(passwordConfirm = "password")) {}
            Spacer(Modifier.height(20.dp))
            PasswordConfirmInputContent(
                SignUpUiState(
                    passwordConfirm = "password1",
                    passwordConfirmErrorMessage = "비밀번호가 일치하지 않습니다."
                )
            ) {}
        }
    }
}

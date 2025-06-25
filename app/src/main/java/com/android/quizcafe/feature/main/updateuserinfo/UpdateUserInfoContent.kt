package com.android.quizcafe.feature.main.updateuserinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Text
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
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.AnimatedTitleWithBody
import kotlinx.coroutines.delay

@Composable
fun UpdateNicknameContent(
    state: UpdateUserInfoUiState,
    intent: (UpdateUserInfoIntent) -> Unit,
    innerPadding: PaddingValues
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    AnimatedTitleWithBody(
        title = stringResource(R.string.update_nickname_title),
        innerPadding = innerPadding,
        content = {
            LabeledInputField(
                label = stringResource(R.string.nickname),
                value = state.nickname,
                onValueChange = { intent(UpdateUserInfoIntent.UpdatedNickname(it)) },
                focusRequester = focusRequester,
                errorMessage = state.nicknameError
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    )
}

@Composable
fun UpdatePasswordContent(
    state: UpdateUserInfoUiState,
    intent: (UpdateUserInfoIntent) -> Unit,
    innerPadding: PaddingValues
) {
    val currentFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(100)
        currentFocus.requestFocus()
    }

    AnimatedTitleWithBody(
        title = stringResource(R.string.update_password_title),
        innerPadding = innerPadding,
        content = {
            LabeledInputField(
                label = stringResource(R.string.current_password),
                value = state.currentPassword,
                onValueChange = { intent(UpdateUserInfoIntent.UpdatedCurrentPassword(it)) },
                isPassword = true,
                errorMessage = state.currentPasswordError,
                focusRequester = currentFocus
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledInputField(
                label = stringResource(R.string.new_password),
                value = state.newPassword,
                onValueChange = { intent(UpdateUserInfoIntent.UpdatedNewPassword(it)) },
                isPassword = true,
                errorMessage = state.newPasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledInputField(
                label = stringResource(R.string.new_password_confirm),
                value = state.confirmPassword,
                onValueChange = { intent(UpdateUserInfoIntent.UpdatedConfirmPassword(it)) },
                isPassword = true,
                errorMessage = state.confirmPasswordError
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    )
}

@Composable
fun BottomActionButton(
    step: Int,
    state: UpdateUserInfoUiState,
    onClick: () -> Unit
) {
    val isEnabled = when (step) {
        0 -> state.isUpdateNicknameEnabled
        1 -> state.isUpdatePasswordEnabled
        else -> false
    }

    val label = when (step) {
        0 -> stringResource(R.string.update_nickname_button)
        1 -> stringResource(R.string.update_password_button)
        else -> "변경"
    }

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
            enabled = isEnabled
        ) {
            Text(text = label)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateNicknameContentPreview() {
    QuizCafeTheme {
        Column {
            UpdateNicknameContent(
                state = UpdateUserInfoUiState(nickname = ""),
                intent = {},
                innerPadding = PaddingValues(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdatePasswordContentPreview() {
    QuizCafeTheme {
        Column {
            UpdatePasswordContent(
                state = UpdateUserInfoUiState(
                    currentPassword = "",
                    newPassword = "",
                    confirmPassword = ""
                ),
                intent = {},
                innerPadding = PaddingValues(16.dp)
            )
        }
    }
}

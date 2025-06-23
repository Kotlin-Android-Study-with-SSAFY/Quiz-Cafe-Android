package com.android.quizcafe.feature.main.updateuserinfo

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.ui.QuizCafeTopAppBar
import com.android.quizcafe.core.ui.TopAppBarTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserInfoScreen(
    step: Int,
    state: UpdateUserInfoViewState,
    intent: (UpdateUserInfoIntent) -> Unit = {},
    onNavigateBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        topBar = {
            QuizCafeTopAppBar(
                title = TopAppBarTitle.Text(stringResource(R.string.update_user_info)),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
                step = step,
                state = state,
                onClick = {
                    when (step) {
                        0 -> intent(UpdateUserInfoIntent.ConfirmNickname)
                        1 -> intent(UpdateUserInfoIntent.ConfirmPassword)
                    }
                }
            )
        }
    ) { padding ->
        AnimatedContent(targetState = step) { current ->
            when (current) {
                0 -> UpdateNicknameContent(
                    state = state,
                    intent = intent,
                    innerPadding = padding
                )

                1 -> UpdatePasswordContent(
                    state = state,
                    intent = intent,
                    innerPadding = padding
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "닉네임 변경 - 기본")
@Composable
fun PreviewUpdateUserInfoScreen_Nickname() {
    UpdateUserInfoScreen(
        step = 0,
        state = UpdateUserInfoViewState(
            nickname = "새로운닉네임",
            nicknameError = null,
            isUpdateNicknameEnabled = true
        ),
        intent = {},
        onNavigateBack = {}
    )
}

@Preview(showBackground = true, name = "닉네임 변경 - 에러")
@Composable
fun PreviewUpdateUserInfoScreen_Nickname_Error() {
    UpdateUserInfoScreen(
        step = 0,
        state = UpdateUserInfoViewState(
            nickname = "짧음",
            nicknameError = "닉네임은 2자 이상이어야 합니다.",
            isUpdateNicknameEnabled = false
        ),
        intent = {},
        onNavigateBack = {}
    )
}

@Preview(showBackground = true, name = "비밀번호 변경 - 기본")
@Composable
fun PreviewUpdateUserInfoScreen_Password() {
    UpdateUserInfoScreen(
        step = 1,
        state = UpdateUserInfoViewState(
            currentPassword = "",
            newPassword = "",
            confirmPassword = "",
            isUpdatePasswordEnabled = false
        ),
        intent = {},
        onNavigateBack = {}
    )
}

@Preview(showBackground = true, name = "비밀번호 변경 - 에러")
@Composable
fun PreviewUpdateUserInfoScreen_Password_Error() {
    UpdateUserInfoScreen(
        step = 1,
        state = UpdateUserInfoViewState(
            currentPassword = "wrongpw",
            newPassword = "123",
            confirmPassword = "12",
            currentPasswordError = "기존 비밀번호를 입력해주세요.",
            newPasswordError = "비밀번호는 8~20자의 영문, 숫자, 특수문자를 포함해야 합니다.",
            confirmPasswordError = "비밀번호가 일치하지 않습니다.",
            isUpdatePasswordEnabled = false
        ),
        intent = {},
        onNavigateBack = {}
    )
}

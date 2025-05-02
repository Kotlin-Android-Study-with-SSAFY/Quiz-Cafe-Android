package com.android.quizcafe.feature.signup

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.QuizCafeTextField
import kotlinx.coroutines.delay


@Composable
fun BottomActionButton(state: SignUpViewState, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().imePadding().navigationBarsPadding()
    ) {
        QuizCafeButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = if (state.isSuccessVerification) state.isSignUpEnabled else state.isNextEnabled
        ) {
            Text(stringResource(R.string.next))
        }
    }
}

@Composable
fun EmailInputContent(viewModel: SignUpViewModel, state: SignUpViewState, innerPadding: PaddingValues) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }
    val animatedSpacerPadding by animateDpAsState(if (state.isCodeSent) 0.dp else 40.dp)

    TitleWithAnimation(
        title = stringResource(R.string.verify_email),
        innerPadding = innerPadding,
        content = {
            Spacer(modifier = Modifier.height(animatedSpacerPadding))
            LabeledInputField(
                label = stringResource(R.string.email),
                value = state.email,
                onValueChange = { viewModel.onIntent(SignUpIntent.UpdatedEmail(it)) },
                errorMessage = state.emailErrorMessage,
                focusRequester = focusRequester,
                enabled = !state.isCodeSent
            )

            if (state.isCodeSent) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(stringResource(R.string.verification_code), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                VerificationInputField(state) {
                    viewModel.onIntent(SignUpIntent.UpdatedVerificationCode(it))
                }
            }
        }
    )
}

@Composable
fun PasswordInputContent(viewModel: SignUpViewModel, state: SignUpViewState, innerPadding: PaddingValues) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    TitleWithAnimation(
        title = stringResource(R.string.input_password),
        innerPadding = innerPadding,
        content = {
            LabeledInputField(
                label = stringResource(R.string.password),
                value = state.password,
                onValueChange = { viewModel.onIntent(SignUpIntent.UpdatedPassword(it)) },
                isPassword = true,
                focusRequester = focusRequester,
                errorMessage = state.passwordErrorMessage
            )

            Spacer(modifier = Modifier.height(32.dp))
            LabeledInputField(
                label = stringResource(R.string.password_confirm),
                value = state.passwordConfirm,
                onValueChange = { viewModel.onIntent(SignUpIntent.UpdatedPasswordConfirm(it)) },
                isPassword = true,
                errorMessage = state.passwordConfirmErrorMessage
            )
        }
    )
}

@Composable
fun TitleWithAnimation(
    title: String,
    innerPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = imeBottom > 0

    val animatedTitlePadding by animateDpAsState(if (isKeyboardVisible) 16.dp else 48.dp)
    val animatedSectionPadding by animateDpAsState(if (isKeyboardVisible) 32.dp else 80.dp)

    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(animatedTitlePadding))
        Text(title, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(animatedSectionPadding))
        content()
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun LabeledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    focusRequester: FocusRequester? = null
) {
    Text(label, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(8.dp))
    QuizCafeTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        isPassword = isPassword,
        errorMessage = errorMessage,
        enabled = enabled
    )
}

@Composable
fun VerificationInputField(
    state: SignUpViewState,
    onValueChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    QuizCafeTextField(
        value = state.verificationCode,
        onValueChange = onValueChanged,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(modifier = Modifier.height(40.dp))
}

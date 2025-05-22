package com.android.quizcafe.feature.signup

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.LabeledInputField
import com.android.quizcafe.core.designsystem.QuizCafeTextField
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.onSurfaceVariantLight
import com.android.quizcafe.core.ui.AnimatedTitleWithBody
import kotlinx.coroutines.delay

@Composable
fun EmailInputContent(
    state: SignUpViewState,
    sendIntent: (SignUpIntent) -> Unit,
    innerPadding: PaddingValues
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    AnimatedTitleWithBody(
        title = stringResource(R.string.verify_email),
        innerPadding = innerPadding,
        content = {
            val animatedSpacerPadding by animateDpAsState(if (state.isCodeSent) 0.dp else 40.dp)
            Spacer(modifier = Modifier.height(animatedSpacerPadding))

            EmailInputContent(state, focusRequester, sendIntent)

            if (state.isCodeSent) {
                Spacer(modifier = Modifier.height(32.dp))
                VerificationContent(state, sendIntent)
            }
        }
    )
}

@Composable
fun EmailInputContent(
    state: SignUpViewState,
    focusRequester: FocusRequester?,
    sendIntent: (SignUpIntent) -> Unit
) {
    LabeledInputField(
        label = stringResource(R.string.email),
        value = state.email,
        onValueChange = { sendIntent(SignUpIntent.UpdatedEmail(it)) },
        errorMessage = state.emailErrorMessage,
        focusRequester = focusRequester,
        enabled = !state.isCodeSent
    )
}

@Composable
fun VerificationContent(
    state: SignUpViewState,
    sendIntent: (SignUpIntent) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.verification_code), fontSize = 20.sp)
            Text(
                stringResource(R.string.resend),
                modifier = Modifier.clickable {
                    sendIntent(SignUpIntent.ClickSendCode)
                },
                fontSize = 16.sp,
                color = onSurfaceVariantLight
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        VerificationInputField(state) {
            sendIntent(SignUpIntent.UpdatedVerificationCode(it))
        }

        VerificationTimer(state)
    }
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
}

@SuppressLint("DefaultLocale")
@Composable
fun VerificationTimer(state: SignUpViewState) {
    val time = state.remainingSeconds
    val formatted = String.format("%02d:%02d", time / 60, time % 60)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = formatted,
            modifier = Modifier
                .align(Alignment.End)
                .padding(4.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmailInputContentPreview() {
    QuizCafeTheme {
        Column {
            EmailInputContent(SignUpViewState(), null) {}
            Spacer(Modifier.height(20.dp))
            EmailInputContent(SignUpViewState(email = "email", emailErrorMessage = "이메일 형식이 올바르지 않습니다."), null) {}
            Spacer(Modifier.height(20.dp))
            EmailInputContent(SignUpViewState(email = "email@email.com"), null) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationInputFieldPreview() {
    QuizCafeTheme {
        Column {
            VerificationContent(SignUpViewState()) {}
            Spacer(Modifier.height(20.dp))
            VerificationContent(SignUpViewState(verificationCode = "123456")) {}
        }
    }
}

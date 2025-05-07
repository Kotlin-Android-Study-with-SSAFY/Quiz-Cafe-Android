package com.android.quizcafe.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.QuizCafeTextField
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme


@Composable
fun QuizCafeLogo() {
    Image(
        painter = painterResource(id = R.drawable.quizcafelogo),
        contentDescription = stringResource(R.string.quizcafe_logo),
        modifier = Modifier
            .padding(top = 20.dp)
            .height(140.dp)
    )
}

@Composable
fun EmailInputContent(state: LoginViewState, onIntent: (LoginIntent) -> Unit) {
    QuizCafeTextField(
        label = "ID",
        value = state.email,
        onValueChange = { onIntent(LoginIntent.UpdatedEmail(it)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

@Composable
fun PasswordInputContent(state: LoginViewState, onIntent: (LoginIntent) -> Unit) {
    QuizCafeTextField(
        label = "PW",
        value = state.password,
        onValueChange = { onIntent(LoginIntent.UpdatedPassword(it)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        isPassword = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}



@Composable
fun LoginButton(state: LoginViewState, onClick: () -> Unit) {
    QuizCafeButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = state.isLoginEnabled
    ) {
        Text(text = stringResource(R.string.login))
    }
}

@Composable
fun BottomTextOptions(onIntent: (LoginIntent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = stringResource(R.string.forgot_password),
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.signup),
            fontSize = 14.sp,
            modifier = Modifier.clickable { onIntent(LoginIntent.ClickSignUp) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCafeLogoPreview() {
    QuizCafeTheme {
        QuizCafeLogo()
    }
}

@Preview(showBackground = true)
@Composable
fun EmailInputPreview() {
    QuizCafeTheme {
        Column {
            EmailInputContent(LoginViewState()) {}
            Spacer(Modifier.height(20.dp))
            EmailInputContent(LoginViewState(email = "email")) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordInputPreview() {
    QuizCafeTheme {
        Column {
            PasswordInputContent(LoginViewState()) {}
            Spacer(Modifier.height(20.dp))
            PasswordInputContent(LoginViewState(password = "password")) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginButtonPreview() {
    QuizCafeTheme {
        Column {
            LoginButton(LoginViewState()) {}
            Spacer(Modifier.height(20.dp))
            LoginButton(LoginViewState(isLoginEnabled = true)) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomTextOptionsPreview() {
    QuizCafeTheme {
        BottomTextOptions {}
    }
}
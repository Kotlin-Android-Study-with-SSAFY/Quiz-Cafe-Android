package com.android.quizcafe.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton


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
fun LoginButton(onClick: () -> Unit, state: LoginViewState) {
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
fun BottomTextOptions(viewModel: LoginViewModel) {
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
            modifier = Modifier.clickable { viewModel.onIntent(LoginIntent.ClickSignUp) }
        )
    }
}
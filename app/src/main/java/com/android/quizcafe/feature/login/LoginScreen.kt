package com.android.quizcafe.feature.login

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.QuizCafeTextField
import com.android.quizcafe.core.designsystem.theme.Typography
import com.android.quizcafe.core.designsystem.theme.errorLight
import com.android.quizcafe.core.designsystem.theme.primaryContainerLight
import com.android.quizcafe.core.designsystem.theme.primaryLight
import com.android.quizcafe.core.designsystem.theme.surfaceBrightLight

@Composable
fun LoginScreen(
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsState()

    // 키보드 크기 계산
    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomDp = with(LocalDensity.current) { imeBottomPx.toDp() }

    // 키보드 올라올 때 패딩 애니메이션 적용
    val animatedPadding by animateDpAsState(
        targetValue = imeBottomDp,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "AnimatedImePadding"
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToHome -> {
                    navigateToHome()
                    Toast.makeText(
                        context,
                        context.getString(R.string.success_login), Toast.LENGTH_SHORT
                    ).show()
                }

                LoginEffect.NavigateToSignUp -> {
                    navigateToSignUp()
                }

                is LoginEffect.ShowErrorDialog -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp).padding(bottom = animatedPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            QuizCafeLogo()
            QuizCafeTextField(
                label = "ID",
                value = state.email,
                onValueChange = { viewModel.onIntent(LoginIntent.UpdatedEmail(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            QuizCafeTextField(
                label = "PW",
                value = state.password,
                onValueChange = { viewModel.onIntent(LoginIntent.UpdatedPassword(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(24.dp))
            LoginButton(
                onClick = {
                    viewModel.onIntent(LoginIntent.ClickLogin)
                },
                state = state
            )

            Spacer(modifier = Modifier.height(16.dp))
            BottomTextOptions(viewModel)

            Spacer(modifier = Modifier.height(40.dp))

        }
    }
}


@Composable
fun QuizCafeLogo() {
    Image(
        painter = painterResource(id = R.drawable.quizcafelogo),
        contentDescription = "QuizCafe Logo",
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

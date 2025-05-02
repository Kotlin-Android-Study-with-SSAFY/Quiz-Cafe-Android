package com.android.quizcafe.feature.login

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeTextField

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
        targetValue = imeBottomDp, animationSpec = tween(
            durationMillis = 400, easing = FastOutSlowInEasing
        ), label = "AnimatedImePadding"
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToHome -> {
                    navigateToHome()
                    Toast.makeText(
                        context, context.getString(R.string.success_login), Toast.LENGTH_SHORT
                    ).show()
                }

                LoginEffect.NavigateToSignUp -> {
                    navigateToSignUp()
                }

                is LoginEffect.ShowErrorDialog -> {
                    Toast.makeText(
                        context, context.getString(R.string.error_message), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = animatedPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            QuizCafeLogo()
        }
        item {
            QuizCafeTextField(
                label = "ID",
                value = state.email,
                onValueChange = { viewModel.onIntent(LoginIntent.UpdatedEmail(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
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
        }
        item {
            LoginButton(
                onClick = {
                    viewModel.onIntent(LoginIntent.ClickLogin)
                }, state = state
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            BottomTextOptions(viewModel)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navigateToSignUp = { }, navigateToHome = {})
}
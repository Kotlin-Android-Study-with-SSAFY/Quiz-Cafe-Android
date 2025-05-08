package com.android.quizcafe.feature.login

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

@Composable
fun LoginScreen(
    state: LoginViewState,
    onIntent: (LoginIntent) -> Unit
) {
    // 키보드 크기 계산
    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomDp = with(LocalDensity.current) { imeBottomPx.toDp() }

    // 키보드 올라올 때 패딩 애니메이션 적용
    val animatedPadding by animateDpAsState(
        targetValue = imeBottomDp, animationSpec = tween(
            durationMillis = 400, easing = FastOutSlowInEasing
        ), label = "AnimatedImePadding"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = animatedPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            QuizCafeLogo()
        }
        item {
            EmailInputContent(state, onIntent)
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            PasswordInputContent(state, onIntent)
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            LoginButton(
                onClick = {
                    onIntent(LoginIntent.ClickLogin)
                }, state = state
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            BottomTextOptions(onIntent)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    QuizCafeTheme {
        LoginScreen(LoginViewState()) {}
    }
}

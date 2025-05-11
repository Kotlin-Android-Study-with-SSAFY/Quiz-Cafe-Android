package com.android.quizcafe.core.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.quizcafe.core.designsystem.theme.outlineVariantLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography

@Composable
fun AnimatedTitleWithBody(
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
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(animatedTitlePadding))
        Text(title, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(animatedSectionPadding))
        content()
        Spacer(modifier = Modifier.height(40.dp))
    }
}

// TopAppBar를 사용하지 않는 underline Title
@Composable
fun TitleWithUnderLine(title: String) {
    Spacer(Modifier.height(60.dp))
    Text(
        text = title,
        style = quizCafeTypography().titleLarge
    )
    Spacer(Modifier.height(16.dp))
    HorizontalDivider(thickness = 1.dp, color = outlineVariantLight)
}

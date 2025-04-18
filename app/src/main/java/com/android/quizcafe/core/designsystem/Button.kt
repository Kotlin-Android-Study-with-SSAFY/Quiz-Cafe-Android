package com.android.quizcafe.core.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


// TODO: 버튼에 대해서 조금 더 고려를 할 것이 많음.

/**
 * QuizCafe의 기본 버튼 컴포넌트입니다. 내부에 콘텐츠 슬롯을 받아 자유롭게 커스터마이징이 가능합니다.
 */
@Composable
fun QuizCafeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * QuizCafe의 텍스트 및 아이콘이 함께 들어가는 기본 버튼입니다.
 */
@Composable
fun QuizCafeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    QuizCafeButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) ButtonDefaults.ButtonWithIconContentPadding else ButtonDefaults.ContentPadding
    ) {
        QuizCafeButtonContent(text = text, leadingIcon = leadingIcon)
    }
}

/**
 * QuizCafe의 외곽선 버튼 컴포넌트입니다. 콘텐츠 슬롯을 받습니다.
 */
@Composable
fun QuizCafeOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
        border = BorderStroke(
            width = QuizCafeButtonDefaults.OutlinedButtonBorderWidth,
            color = if (enabled) MaterialTheme.colorScheme.outline
            else MaterialTheme.colorScheme.onSurface.copy(alpha = QuizCafeButtonDefaults.DISABLED_OUTLINED_BUTTON_BORDER_ALPHA)
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * QuizCafe의 외곽선 버튼이며 텍스트와 아이콘을 함께 사용할 수 있습니다.
 */
@Composable
fun QuizCafeOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    QuizCafeOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) ButtonDefaults.ButtonWithIconContentPadding else ButtonDefaults.ContentPadding
    ) {
        QuizCafeButtonContent(text = text, leadingIcon = leadingIcon)
    }
}

/**
 * QuizCafe의 텍스트 버튼입니다. 기본 텍스트 버튼과 동일하게 사용 가능합니다.
 */
@Composable
fun QuizCafeTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
        content = content
    )
}

/**
 * QuizCafe의 텍스트와 아이콘이 있는 텍스트 버튼입니다.
 */
@Composable
fun QuizCafeTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    QuizCafeTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        QuizCafeButtonContent(text = text, leadingIcon = leadingIcon)
    }
}

/**
 * 버튼 안에서 텍스트와 아이콘을 배치하기 위한 내부 레이아웃입니다.
 */
@Composable
private fun QuizCafeButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    leadingIcon?.let {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) { it() }
    }
    Box(
        Modifier.padding(start = if (leadingIcon != null) ButtonDefaults.IconSpacing else 0.dp)
    ) {
        text()
    }
}

/**
 * 버튼 공통 기본값 정의
 */
object QuizCafeButtonDefaults {
    const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f
    val OutlinedButtonBorderWidth = 1.dp
}

// -------- Preview Code --------

@Preview
@Composable
fun PreviewQuizCafeButton() {
    QuizCafeButton(onClick = {}) {
        Text("기본 버튼")
    }
}

@Preview
@Composable
fun PreviewQuizCafeButtonWithIcon() {
    QuizCafeButton(onClick = {}, text = { Text("아이콘 버튼") }, leadingIcon = {
        Icon(
            imageVector = Icons.Default.Favorite, contentDescription = null
        )
    })
}

@Preview
@Composable
fun PreviewQuizCafeOutlinedButton() {
    QuizCafeOutlinedButton(onClick = {}) {
        Text("외곽선 버튼")
    }
}

@Preview
@Composable
fun PreviewQuizCafeTextButton() {
    QuizCafeTextButton(onClick = {}) {
        Text("텍스트 버튼")
    }
}

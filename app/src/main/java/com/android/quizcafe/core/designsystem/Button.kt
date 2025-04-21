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

/**
 * QuizCafe의 기본 버튼 컴포넌트입니다.
 *
 * @param onClick 버튼 클릭 시 실행할 람다
 * @param modifier Modifier를 통해 외부에서 스타일 조정 가능
 * @param enabled 버튼 활성화 여부
 * @param text 버튼 내 텍스트 콘텐츠
 * @param leadingIcon 텍스트 앞에 배치할 아이콘
 * @param contentPadding 버튼 콘텐츠 패딩
 */
@Composable
fun QuizCafeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = when {
        leadingIcon != null && text != null -> ButtonDefaults.ButtonWithIconContentPadding
        else -> ButtonDefaults.ContentPadding
    }
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = contentPadding,
    ) {
        QuizCafeButtonContent(text = text, leadingIcon = leadingIcon)
    }
}

/**
 * QuizCafe의 외곽선 버튼입니다.
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
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
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
 * QuizCafe의 텍스트 버튼입니다.
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
 * 버튼 내부에서 텍스트 및 아이콘을 배치하는 기본 레이아웃입니다.
 */
@Composable
private fun RowScope.QuizCafeButtonContent(
    text: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) { leadingIcon() }
    }

    if (text != null) {
        Box(
            Modifier.padding(start = if (leadingIcon != null) ButtonDefaults.IconSpacing else 0.dp)
        ) {
            text()
        }
    }
}

/**
 * 버튼 공통 스타일 정의
 */
object QuizCafeButtonDefaults {
    const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f
    val OutlinedButtonBorderWidth = 1.dp
}

@Preview
@Composable
fun PreviewQuizCafeButton_TextOnly() {
    QuizCafeButton(
        onClick = {},
        text = { Text("기본 버튼") }
    )
}

@Preview
@Composable
fun PreviewQuizCafeButton_IconWithText() {
    QuizCafeButton(
        onClick = {},
        text = { Text("아이콘 버튼") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
        }
    )
}

@Preview
@Composable
fun PreviewQuizCafeButton_IconOnly() {
    QuizCafeButton(
        onClick = {},
        text = null,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
        }
    )
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


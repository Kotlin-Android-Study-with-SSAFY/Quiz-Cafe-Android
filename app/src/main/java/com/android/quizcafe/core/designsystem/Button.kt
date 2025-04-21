package com.android.quizcafe.core.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * QuizCafe의 기본 버튼입니다.
 *
 * @param onClick 버튼 클릭 이벤트
 * @param modifier Modifier를 통해 크기/마진 조절
 * @param enabled 버튼 활성화 여부
 * @param text 텍스트 컴포저블 (필수)
 * @param leadingIcon 텍스트 앞에 위치할 아이콘 (옵션)
 * @param contentPadding 콘텐츠 패딩
 */
@Composable
fun QuizCafeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = if (leadingIcon != null) {
        ButtonDefaults.ButtonWithIconContentPadding
    } else {
        ButtonDefaults.ContentPadding
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
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        content = content
    )
}

/**
 * 버튼 내부에서 아이콘과 텍스트를 배치하는 레이아웃
 */
@Composable
private fun QuizCafeButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)?,
) {
    Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
                it()
            }
        }

        Box(
            Modifier.padding(start = if (leadingIcon != null) ButtonDefaults.IconSpacing else 0.dp)
        ) {
            text()
        }
    }
}

/**
 * 버튼 스타일 공통 정의
 */
object QuizCafeButtonDefaults {
    const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f
    val OutlinedButtonBorderWidth = 1.dp
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizCafeButton_TextOnly() {
    QuizCafeButton(onClick = {}, text = { Text("기본 버튼") })
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizCafeButton_IconWithText() {
    QuizCafeButton(onClick = {}, text = { Text("아이콘 버튼") }, leadingIcon = {
        Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizCafeOutlinedButton() {
    QuizCafeOutlinedButton(onClick = {}) {
        Text("외곽선 버튼")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizCafeTextButton() {
    QuizCafeTextButton(onClick = {}) {
        Text("텍스트 버튼")
    }
}

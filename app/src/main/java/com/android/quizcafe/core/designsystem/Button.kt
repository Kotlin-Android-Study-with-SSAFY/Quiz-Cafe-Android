package com.android.quizcafe.core.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

/**
 * 기본 버튼 컴포저블 - QuizCafe의 대표적인 Primary 버튼.
 *
 * @param onClick 버튼 클릭 시 호출될 콜백
 * @param modifier 외부 레이아웃 조정용 Modifier
 * @param enabled 버튼 활성화 여부
 * @param text 텍스트 콘텐츠 컴포저블 (필수)
 * @param leadingIcon 텍스트 앞에 표시될 아이콘 (선택)
 * @param shape 버튼의 모서리 둥글기
 * @param contentPadding 콘텐츠 내부 여백
 */
@Composable
fun QuizCafeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    contentPadding: PaddingValues =
        if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else {
            ButtonDefaults.ContentPadding
        },
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = contentPadding,
    ) {
        QuizCafeButtonContent(text = text, leadingIcon = leadingIcon)
    }
}

/**
 * 외곽선 버튼 - 보조 액션에 적합한 스타일의 버튼.
 *
 * @param onClick 클릭 이벤트 콜백
 * @param modifier Modifier 지정
 * @param enabled 활성화 여부
 * @param contentPadding 내부 여백
 * @param content RowScope를 이용한 콘텐츠 Slot
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
        border =
        BorderStroke(
            width = QuizCafeButtonDefaults.OutlinedButtonBorderWidth,
            color =
            if (enabled) {
                MaterialTheme.colorScheme.outline
            } else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = QuizCafeButtonDefaults.DISABLED_OUTLINED_BUTTON_BORDER_ALPHA,
                )
            },
        ),
        colors =
        ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * 텍스트 버튼 - 텍스트만 표시되는 버튼.
 *
 * @param onClick 클릭 이벤트 콜백
 * @param modifier Modifier 지정
 * @param enabled 활성화 여부
 * @param content RowScope를 이용한 콘텐츠 Slot
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
        colors =
        ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        content = content,
    )
}

/**
 * 버튼 내부 콘텐츠 구성 - 아이콘이 있을 경우 앞에 배치.
 */
@Composable
private fun QuizCafeButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)?,
) {
    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        leadingIcon?.let {
            Box(modifier = Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
                it()
            }
        }
        Box(
            modifier =
            Modifier.padding(
                start = if (leadingIcon != null) ButtonDefaults.IconSpacing else 0.dp,
            ),
        ) {
            text()
        }
    }
}

/**
 * 버튼 스타일 공통 상수 정의
 */
object QuizCafeButtonDefaults {
    const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f
    val OutlinedButtonBorderWidth = 1.dp
}

/**
 * Preview 영역
 */

@Preview(showBackground = true, name = "텍스트 버튼 - 기본")
@Composable
fun Preview_QuizCafeButton_TextOnly() {
    QuizCafeTheme {
        QuizCafeButton(
            onClick = {},
            text = { Text("기본 버튼") },
        )
    }
}

@Preview(showBackground = true, name = "텍스트 버튼 - Material 기본 shape")
@Composable
fun Preview_QuizCafeButton_DefaultShape() {
    QuizCafeTheme {
        QuizCafeButton(
            onClick = {},
            text = { Text("적용") },
            shape = ButtonDefaults.shape,
        )
    }
}

@Preview(showBackground = true, name = "아이콘 포함 버튼")
@Composable
fun Preview_QuizCafeButton_IconWithText() {
    QuizCafeTheme {
        QuizCafeButton(
            onClick = {},
            text = { Text("아이콘 버튼") },
            leadingIcon = {
                Icon(Icons.Filled.Favorite, contentDescription = null)
            },
        )
    }
}

@Preview(showBackground = true, name = "외곽선 버튼")
@Composable
fun Preview_QuizCafeOutlinedButton() {
    QuizCafeTheme {
        QuizCafeOutlinedButton(onClick = {}) {
            Text("취소")
        }
    }
}

@Preview(showBackground = true, name = "텍스트 버튼")
@Composable
fun Preview_QuizCafeTextButton() {
    QuizCafeTheme {
        QuizCafeTextButton(onClick = {}) {
            Text("텍스트 버튼")
        }
    }
}

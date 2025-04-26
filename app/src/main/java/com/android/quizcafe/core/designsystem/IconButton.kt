package com.android.quizcafe.core.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

/**
 * QuizCafe의 토글 가능한 아이콘 버튼입니다.
 *
 * @param checked 현재 체크 상태
 * @param onCheckedChange 체크 상태가 변경될 때 호출됨
 * @param modifier Modifier
 * @param enabled 활성화 여부
 * @param icon 체크되지 않은 상태의 아이콘
 * @param checkedIcon 체크된 상태의 아이콘 (기본값: icon 동일)
 */
@Composable
fun QuizCafeIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon,
) {
    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors =
            IconButtonDefaults.iconToggleButtonColors(
                checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                checkedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor =
                    if (checked) {
                        MaterialTheme.colorScheme.onBackground.copy(
                            alpha = QuizCafeIconButtonDefaults.DISABLED_ICON_BUTTON_CONTAINER_ALPHA,
                        )
                    } else {
                        Color.Transparent
                    },
            ),
    ) {
        if (checked) checkedIcon() else icon()
    }
}

/**
 * QuizCafe 아이콘 버튼 기본값
 */
object QuizCafeIconButtonDefaults {
    const val DISABLED_ICON_BUTTON_CONTAINER_ALPHA = 0.12f
}

@Preview(showBackground = true)
@Composable
fun QuizCafeIconToggleButtonPreview_Checked() {
    QuizCafeTheme {
        QuizCafeIconToggleButton(
            checked = true,
            onCheckedChange = {},
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "즐겨찾기 해제",
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "즐겨찾기",
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCafeIconToggleButtonPreview_Unchecked() {
    QuizCafeTheme {
        QuizCafeIconToggleButton(
            checked = false,
            onCheckedChange = {},
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "즐겨찾기 해제",
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "즐겨찾기",
                )
            },
        )
    }
}

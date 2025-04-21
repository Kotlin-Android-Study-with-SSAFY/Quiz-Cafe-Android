@file:OptIn(ExperimentalMaterial3Api::class)

package com.android.quizcafe.core.designsystem

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

/**
 * QuizCafe의 공통 TopAppBar 컴포저블입니다.
 *
 * @param title 텍스트 문자열. titleRes 대신 직접 문자열 전달 가능
 * @param titleRes stringRes로 전달할 경우 사용 (title와 동시 사용 불가)
 * @param navigationIcon 네비게이션 아이콘 (null이면 숨김)
 * @param navigationIconContentDescription 접근성 설명
 * @param actionIcon 우측 액션 아이콘 (null이면 숨김)
 * @param actionIconContentDescription 접근성 설명
 * @param modifier Modifier
 * @param testTag 테스트 식별자
 * @param colors TopAppBar 색상 커스터마이징
 * @param onNavigationClick 네비게이션 아이콘 클릭 이벤트
 * @param onActionClick 액션 아이콘 클릭 이벤트
 */
@Composable
fun QuizCafeTopAppBar(
    title: String? = null,
    @StringRes titleRes: Int? = null,
    navigationIcon: ImageVector? = null,
    navigationIconContentDescription: String? = null,
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String? = null,
    modifier: Modifier = Modifier,
    testTag: String = "quizCafeTopAppBar",
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    require(title != null || titleRes != null) {
        "Either title or titleRes must be provided"
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title ?: stringResource(id = titleRes!!),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconContentDescription
                    )
                }
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription
                    )
                }
            }
        },
        colors = colors,
        modifier = modifier.testTag(testTag)
    )
}

object QuizCafeIcons {
    val ArrowBack: ImageVector = Icons.Filled.ArrowBack
    val MoreVert: ImageVector = Icons.Filled.MoreVert
}

@Preview(showBackground = true)
@Composable
private fun QuizCafeTopAppBarPreview() {
    QuizCafeTheme {
        QuizCafeTopAppBar(
            titleRes = android.R.string.untitled,
            navigationIcon = QuizCafeIcons.ArrowBack,
            navigationIconContentDescription = "뒤로가기",
            actionIcon = QuizCafeIcons.MoreVert,
            actionIconContentDescription = "메뉴"
        )
    }
}

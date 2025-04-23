@file:OptIn(ExperimentalMaterial3Api::class)

package com.android.quizcafe.core.designsystem

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

/**
 * 앱바의 타이틀을 구성할 수 있는 타입.
 * - Text: 일반 텍스트 전달
 * - Resource: string 리소스 ID를 통한 전달
 */
sealed class TopAppBarTitle {
    data class Text(val value: String) : TopAppBarTitle()
    data class Resource(@StringRes val id: Int) : TopAppBarTitle()
}

/**
 * QuizCafe의 공통 CenterAligned TopAppBar 컴포저블입니다.
 *
 * @param title 앱바 중앙 타이틀 (Text or Resource 기반)
 * @param modifier Modifier for outer layout control
 * @param testTag UI 테스트용 태그
 * @param colors 앱바 색상 커스터마이징 (Material3 기본 제공)
 * @param navigationIcon 왼쪽 네비게이션 아이콘 슬롯 (null이면 비표시)
 * @param actions 오른쪽 액션 아이콘 영역 (null이면 비표시)
 */
@Composable
fun QuizCafeTopAppBar(
    title: TopAppBarTitle,
    modifier: Modifier = Modifier,
    testTag: String = "quizCafeTopAppBar",
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            // title 타입에 따라 분기 처리
            when (title) {
                is TopAppBarTitle.Text -> Text(
                    text = title.value,
                    style = MaterialTheme.typography.titleLarge
                )

                is TopAppBarTitle.Resource -> Text(
                    text = stringResource(id = title.id),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            // 네비게이션 아이콘이 있을 경우만 표시
            navigationIcon?.invoke()
        },
        actions = {
            // 우측 액션 아이콘 영역
            actions?.invoke(this)
        },
        colors = colors,
        modifier = modifier.testTag(testTag)
    )
}

/**
 * QuizCafeTopAppBar의 프리뷰.
 * Android Studio Preview에서 테마와 함께 확인 가능
 */
@Preview(showBackground = true, name = "기본형 앱바")
@Composable
private fun Preview_DefaultTopAppBar() {
    QuizCafeTheme {
        QuizCafeTopAppBar(
            title = TopAppBarTitle.Text("기본 타이틀")
        )
    }
}

@Preview(showBackground = true, name = "네비게이션 아이콘 포함")
@Composable
private fun Preview_NavigationIconTopAppBar() {
    QuizCafeTheme {
        QuizCafeTopAppBar(
            title = TopAppBarTitle.Text("뒤로가기 포함"),
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                }
            }
        )
    }
}

@Preview(showBackground = true, name = "액션 아이콘 포함")
@Composable
private fun Preview_ActionIconTopAppBar() {
    QuizCafeTheme {
        QuizCafeTopAppBar(
            title = TopAppBarTitle.Text("메뉴 포함"),
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "메뉴")
                }
            }
        )
    }
}

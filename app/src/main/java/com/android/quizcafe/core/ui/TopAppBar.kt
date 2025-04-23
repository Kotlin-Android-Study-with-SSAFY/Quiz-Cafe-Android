@file:OptIn(ExperimentalMaterial3Api::class)

package com.android.quizcafe.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

/**
 * 앱바의 타이틀을 구성할 수 있는 타입.
 */
sealed class TopAppBarTitle {
    data class Text(val value: String) : TopAppBarTitle()
    data class Resource(@StringRes val id: Int) : TopAppBarTitle()
}

/**
 * QuizCafe의 공통 CenterAligned TopAppBar 컴포저블.
 *
 * @param title 타이틀 텍스트 또는 리소스 ID
 * @param modifier 외부 Modifier
 * @param testTag 테스트 식별 태그
 * @param colors 앱바 색상 지정
 * @param navigationIcon 왼쪽 네비게이션 아이콘 (nullable)
 * @param actions 오른쪽 액션 영역 (nullable)
 * @param alignTitleToStart navigationIcon 및 actions가 없는 경우 좌측 정렬 여부 (기본 false)
 */
@Composable
fun QuizCafeTopAppBar(
    title: TopAppBarTitle,
    modifier: Modifier = Modifier,
    testTag: String = "quizCafeTopAppBar",
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    alignTitleToStart: Boolean = false, // 조건부 좌측 정렬 플래그
) {
    CenterAlignedTopAppBar(
        title = {
            val alignment = if (alignTitleToStart && navigationIcon == null && actions == null) {
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp) // 강제 좌측 정렬 유사
            } else {
                Modifier
            }

            when (title) {
                is TopAppBarTitle.Text -> Text(
                    text = title.value,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = alignment
                )

                is TopAppBarTitle.Resource -> Text(
                    text = stringResource(id = title.id),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = alignment
                )
            }
        },
        navigationIcon = { navigationIcon?.invoke() },
        actions = { actions?.invoke(this) },
        colors = colors,
        modifier = modifier.testTag(testTag)
    )
}

@Preview(showBackground = true, name = "기본형 앱바")
@Composable
private fun Preview_DefaultTopAppBar() {
    QuizCafeTheme {
        QuizCafeTopAppBar(
            title = TopAppBarTitle.Text("기본 타이틀")
        )
    }
}

@Preview(showBackground = true, name = "좌측 정렬 앱바")
@Composable
private fun Preview_LeftAlignedTopAppBar() {
    QuizCafeTheme {
        QuizCafeTopAppBar(
            title = TopAppBarTitle.Text("좌측 정렬"),
            alignTitleToStart = true
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

package com.android.quizcafe.feature.quizbookpicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.TitleWithUnderLine
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizBookPickerScreen() {
    // 임시 데이터
    val sampleQuizBooks = listOf(
        QuizBook(
            id = 1L,
            ownerId = 123456L,
            ownerName = "시스템 관리자",
            category = "운영체제",
            title = "모두의 운영체제",
            difficulty = "???",
            totalQuizzes = 231,
            totalComments = 4,
            totalSaves = 32,
            createdAt = "2025-05-11T15:30:00Z"
        ),
        QuizBook(
            id = 2L,
            ownerId = 654321L,
            ownerName = "싸피_박성준",
            category = "운영체제",
            title = "성준이의 운영체제",
            difficulty = "상",
            totalQuizzes = 30,
            totalComments = 4,
            totalSaves = 32,
            createdAt = "2025-05-11T15:30:00Z"
        )
    )
    val category = "운영체제"

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var isSheetOpen by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TitleWithUnderLine(category)
        Spacer(Modifier.height(4.dp))
        QuizBookListHeader(sampleQuizBooks.size) {
            isSheetOpen = true
        }
        QuizBookCardList(sampleQuizBooks)
        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        isSheetOpen = false
                    }
                },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                QuizBookFilterContent(
                    onApplyClick = {
                        scope.launch {
                            sheetState.hide()
                            isSheetOpen = false
                        }
                        // TODO: 필터 적용하여 리스트 정렬
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookPickerScreenPreview() {
    QuizCafeTheme {
        QuizBookPickerScreen()
    }
}

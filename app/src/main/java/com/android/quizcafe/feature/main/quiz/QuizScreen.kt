package com.android.quizcafe.feature.main.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.QuizCafeTopAppBar
import com.android.quizcafe.core.ui.TopAppBarTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    state: QuizViewState,
    onIntent: (QuizIntent) -> Unit
) {
    Scaffold(
        topBar = {
            QuizCafeTopAppBar(
                title = TopAppBarTitle.Text("Quiz Cafe"),
                alignTitleToStart = true
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 0,
                onItemSelected = { /* TODO: 연결 필요 시 처리 */ }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            QuizHistorySection(historyList = state.historyList)
            Spacer(modifier = Modifier.height(20.dp))
            FeatureSection()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizCafeTheme {
        QuizScreen(
            state = QuizViewState(
                historyList = listOf(
                    QuizHistory("1시간 전", "오인성의 컴파일러 퀴즈", 15, 20)
                )
            ),
            onIntent = {}
        )
    }
}





package com.android.quizcafe.feature.main.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

@Composable
fun QuizScreen(
    state: QuizViewState,
    onIntent: (QuizIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        QuizHistorySection(historyList = state.historyList)
        Spacer(modifier = Modifier.height(20.dp))
        FeatureSection()
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





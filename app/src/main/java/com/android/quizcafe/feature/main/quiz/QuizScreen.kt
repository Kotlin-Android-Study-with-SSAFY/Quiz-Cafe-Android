package com.android.quizcafe.feature.main.quiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

@Composable
fun QuizScreen(
    state: QuizViewState,
    onIntent: (QuizIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { QuizRecordContent(quizRecords = state.quizRecords) }
        item { QuizModeContent() }
        item { Spacer(modifier = Modifier.height(6.dp)) }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizCafeTheme {
        QuizScreen(
            state = QuizViewState(
                quizRecords = listOf(
                    QuizRecord("30분 전", "성준이의 운영체제", 16, 20),
                    QuizRecord("16시간 전", "성민이의 네트워크", 18, 20),
                    QuizRecord("04/01", "재용이의 안드로이드", 19, 20)
                )
            ),
            onIntent = {}
        )
    }
}

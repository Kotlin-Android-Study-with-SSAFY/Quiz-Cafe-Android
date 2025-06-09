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
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord

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
        item { QuizRecordContent(quizSolvingRecords = state.quizSolvingRecords) }
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
                quizSolvingRecords = listOf(
                    QuizSolvingRecord(
                        id = 1,
                        userId = 1,
                        quizBookId = 1,
                        version = 1,
                        level = "EASY",
                        category = "운영체제",
                        title = "성준이의 운영체제",
                        description = "",
                        totalQuizzes = 20,
                        correctCount = 16,
                        completedAt = "2025-06-09T05:14:05.986Z",
                        quizzes = emptyList()
                    ),
                )
            ),
            onIntent = {}
        )
    }
}

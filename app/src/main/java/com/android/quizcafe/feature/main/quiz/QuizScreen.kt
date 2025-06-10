package com.android.quizcafe.feature.main.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.TitleWithUnderLine

@Composable
fun QuizScreen(
    state: QuizUiState,
    sendIntent: (QuizIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        TitleWithUnderLine(stringResource(R.string.tab_title_quiz))
        Spacer(Modifier.height(12.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { QuizHistorySection(historyList = state.historyList) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { QuizModeSection { sendIntent(QuizIntent.ClickQuizCard("")) } }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizCafeTheme {
        QuizScreen(
            state = QuizUiState(
                historyList = listOf(
                    QuizHistory("30분 전", "성준이의 운영체제", 16, 20),
                    QuizHistory("16시간 전", "성민이의 네트워크", 18, 20),
                    QuizHistory("04/01", "재용이의 안드로이드", 19, 20)
                )
            ),
            sendIntent = {}
        )
    }
}

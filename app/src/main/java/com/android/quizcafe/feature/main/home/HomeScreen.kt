package com.android.quizcafe.feature.main.home

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
fun HomeScreen(
    state: HomeUiState,
    sendIntent: (HomeIntent) -> Unit
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
            item { LatestQuizBooksContent(quizBooks = state.latestQuizBooks) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { QuizModeContent { sendIntent(HomeIntent.ClickHomeCard("")) } }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    QuizCafeTheme {
        HomeScreen(
            state = HomeUiState(
                latestQuizBooks = emptyList()
            ),
            sendIntent = {}
        )
    }
}

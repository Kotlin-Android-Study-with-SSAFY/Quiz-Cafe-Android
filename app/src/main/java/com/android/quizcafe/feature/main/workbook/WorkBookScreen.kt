package com.android.quizcafe.feature.main.workbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.LoadingIndicator
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.domain.model.solving.QuizBookGradeWithQuizBook
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.ui.OutLinedOptionSelector
import com.android.quizcafe.core.ui.TitleWithUnderLine

@Composable
fun WorkbookScreen(
    state: WorkBookUiState,
    sendIntent: (WorkBookIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleWithUnderLine(stringResource(R.string.solving_history))
        Spacer(modifier = Modifier.height(16.dp))

        OutLinedOptionSelector(
            modifier = Modifier.fillMaxWidth(),
            options = WorkBookState.entries,
            selectedOption = state.currentWorkBookState,
            onOptionSelected = { sendIntent(WorkBookIntent.UpdateWorkBookState(it)) },
            optionToText = { it.resId }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            LoadingIndicator()
        } else {
            if (state.currentWorkBookState == WorkBookState.SOLVING) {
                SolvingCardList(state.solvingQuizBooks) { sendIntent(WorkBookIntent.ClickSolvingCard(it)) }
            } else {
                SolvedCardList(state.solvedQuizBooks) { sendIntent(WorkBookIntent.ClickSolvedCard(it)) }
            }
        }
    }
}

@Composable
fun SolvingCardList(
    quizBooks: List<QuizBookGradeWithQuizBook>,
    onClick: (QuizBookId) -> Unit
) {
    if (quizBooks.isEmpty()) {
        NoWorkBookContent(stringResource(R.string.no_solving_quizbook))
    }
    LazyColumn {
        items(quizBooks) { solvingBook ->
            WorkBookCard(
                category = solvingBook.quizBook.category,
                title = solvingBook.quizBook.title,
                count = solvingBook.quizBookGrade.quizGrades.size,
                totalQuizzes = solvingBook.quizBook.totalQuizzes,
                completedAt = "",
                isSolved = false,
                onClick = { onClick(QuizBookId(solvingBook.quizBook.id)) }
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun SolvedCardList(
    quizBooks: List<QuizBookSolving>,
    onClick: (QuizBookGradeServerId) -> Unit
) {
    if (quizBooks.isEmpty()) {
        NoWorkBookContent(stringResource(R.string.no_solved_quizbook))
    }
    LazyColumn {
        items(quizBooks) { solvedBook ->
            WorkBookCard(
                category = solvedBook.category,
                title = solvedBook.title,
                count = solvedBook.correctCount,
                totalQuizzes = solvedBook.totalQuizzes,
                completedAt = solvedBook.completedAt,
                isSolved = true,
                onClick = { onClick(QuizBookGradeServerId(solvedBook.id)) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkbookScreenPreview() {
    QuizCafeTheme {
        WorkbookScreen(WorkBookUiState()) {}
    }
}

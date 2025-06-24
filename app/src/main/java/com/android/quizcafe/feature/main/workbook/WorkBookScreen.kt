package com.android.quizcafe.feature.main.workbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.domain.model.solving.QuizBookGradeWithQuizBook
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.ui.TitleWithUnderLine

@Composable
fun WorkbookScreen(
    state: WorkBookUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleWithUnderLine("풀이 기록")
        Spacer(modifier = Modifier.height(20.dp))
        if (state.solvingQuizBooks.isNotEmpty()) {
            SolvingCardList("풀고있어요", state.solvingQuizBooks) { }
        }
        SolvedCardList("풀어봤어요", state.solvedQuizBooks) { }
    }
}

@Composable
fun SolvingCardList(
    title: String,
    quizBooks: List<QuizBookGradeWithQuizBook>,
    onClick: (Long) -> Unit
) {
    Text(title)
    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn {
        items(quizBooks) { solvingBook ->
            WorkBookCard(
                category = solvingBook.quizBook.category,
                title = solvingBook.quizBook.title,
                count = solvingBook.quizBookGrade.quizGrades.size,
                totalQuizzes = solvingBook.quizBook.totalQuizzes,
                completedAt = "",
                isSolved = false,
                onClick = { onClick(solvingBook.quizBookGrade.localId.value) }
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun SolvedCardList(
    title: String,
    quizBooks: List<QuizBookSolving>,
    onClick: (Long) -> Unit
) {
    Text(title)
    Spacer(modifier = Modifier.height(12.dp))

    if (quizBooks.isEmpty()) {
        NoWorkBookContent()
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
                onClick = { onClick(solvedBook.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkbookScreenPreview() {
    QuizCafeTheme {
        WorkbookScreen(WorkBookUiState())
    }
}

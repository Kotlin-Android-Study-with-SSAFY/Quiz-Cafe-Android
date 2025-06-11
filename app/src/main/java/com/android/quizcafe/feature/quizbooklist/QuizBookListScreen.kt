package com.android.quizcafe.feature.quizbooklist

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.TitleWithUnderLine

@Composable
fun QuizBookListScreen(
    state: QuizBookListViewState = QuizBookListViewState(),
    sendIntent: (QuizBookListIntent) -> Unit
) {
    var isSheetOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleWithUnderLine(state.category)
        Spacer(Modifier.height(4.dp))
        QuizBookListHeader(state.filteredQuizBooks.size) {
            isSheetOpen = true
        }
        QuizBookCardList(state.filteredQuizBooks) { quizBookId ->
            sendIntent(QuizBookListIntent.ClickQuizBook(quizBookId))
        }

        if (isSheetOpen) {
            QuizBookFilterBottomSheet(
                filterState = state.currentFilters,
                applyFilter = { filterState ->
                    sendIntent(QuizBookListIntent.UpdateFilterOptions(filterState))
                },
                onDismiss = { isSheetOpen = false },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizBookFilterBottomSheet(
    filterState: FilterState,
    onDismiss: () -> Unit,
    applyFilter: (FilterState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        QuizBookFilterContent(
            onApplyClick = { filterState ->
                applyFilter(filterState)
                onDismiss()
            },
            filterState = filterState,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookListScreenPreview() {
    QuizCafeTheme {
        QuizBookListScreen {}
    }
}

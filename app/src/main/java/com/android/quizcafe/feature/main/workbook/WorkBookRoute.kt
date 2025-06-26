package com.android.quizcafe.feature.main.workbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId

@Composable
fun WorkBookRoute(
    navigateToSolveQuiz: (QuizBookId) -> Unit,
    navigateToGradeResult: (QuizBookGradeServerId) -> Unit,
    viewModel: WorkBookViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.sendIntent(WorkBookIntent.LoadWorkBookList)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is WorkBookEffect.NavigateToSolveQuiz -> navigateToSolveQuiz(it.id)
                is WorkBookEffect.NavigateToGradeResult -> navigateToGradeResult(it.id)
            }
        }
    }

    val workBookState = viewModel.state.collectAsState()
    WorkbookScreen(workBookState.value, viewModel::sendIntent)
}

package com.android.quizcafe.feature.quiz.solve

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.feature.quiz.solve.component.ExitSolvingDialog
import com.android.quizcafe.feature.quiz.solve.component.ResumeSolvingDialog
import com.android.quizcafe.feature.quiz.solve.viewmodel.IQuizSolveViewModel
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveEffect
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveIntent
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveViewModel

@Composable
fun QuizSolveRoute(
    quizBookId: Long,
    navigateToBack: () -> Unit,
    navigateToQuizBookSolvingResult: (QuizBookGradeServerId) -> Unit,
    viewModel: IQuizSolveViewModel = hiltViewModel<QuizSolveViewModel>()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(QuizSolveIntent.LoadQuizBook(quizBookId))
        viewModel.sendIntent(QuizSolveIntent.GetQuizBookLocalId(quizBookId))
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                QuizSolveEffect.ShowResumeDialog -> {
                    showResumeDialog = true
                }
                QuizSolveEffect.CloseResumeDialog -> {
                    showResumeDialog = false
                }
                QuizSolveEffect.ShowExitDialog -> {
                    showExitDialog = true
                }
                QuizSolveEffect.NavigateToBack -> {
                    if (showExitDialog) showExitDialog = false
                    navigateToBack()
                }
                is QuizSolveEffect.NavigateToQuizBookSolvingResult -> {
                    navigateToQuizBookSolvingResult(effect.quizBookGradeServerId)
                }

                is QuizSolveEffect.ShowErrorDialog -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    BackHandler(enabled = !showExitDialog) {
        viewModel.sendIntent(QuizSolveIntent.OnBackClick)
    }

    if (showResumeDialog) {
        ResumeSolvingDialog(
            onResume = {
                viewModel.sendIntent(QuizSolveIntent.ResumeSolving(resumeWithNewSolving = false))
            },
            onStartNew = {
                viewModel.sendIntent(QuizSolveIntent.ResumeSolving(resumeWithNewSolving = true))
            }
        )
    }

    if (showExitDialog) {
        ExitSolvingDialog(
            onDismissRequest = {
                showExitDialog = false
            },
            onExitWithDelete = {
                viewModel.sendIntent(QuizSolveIntent.ExitWithDelete)
            },
            onExitWithSave = {
                viewModel.sendIntent(QuizSolveIntent.ExitWithSave)
            },
        )
    }

    QuizSolveScreen(
        uiState = state,
        onIntent = viewModel::sendIntent
    )
}

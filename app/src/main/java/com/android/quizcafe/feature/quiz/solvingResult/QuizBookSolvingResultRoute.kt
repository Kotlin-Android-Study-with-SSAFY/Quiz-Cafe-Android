package com.android.quizcafe.feature.quiz.solvingResult

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue

@Composable
fun QuizBookSolvingResultRoute(
    quizBookGradeLocalId: Long,
    navigateToMain: () -> Unit,
    viewModel: QuizBookSolvingResultViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(quizBookGradeLocalId) {
        viewModel.sendIntent(QuizBookSolvingResultIntent.InitWithQuizBookGradeId(quizBookGradeLocalId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                QuizBookSolvingResultEffect.NavigateToMain -> {
                    navigateToMain()
                }
                is QuizBookSolvingResultEffect.ShowError -> {
                    Toast.makeText(
                        context,
                        effect.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    QuizBookSolvingResultScreen(
        state = state,
        sendIntent = viewModel::sendIntent
    )
}

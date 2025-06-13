package com.android.quizcafe.feature.quiz.solve

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveEffect
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveIntent
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveViewModel

@Composable
fun QuizSolveRoute(
    quizBookId: Long,
    navigateToBack: () -> Unit,
    viewModel: QuizSolveViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(QuizSolveIntent.LoadQuizBookDetail(quizBookId))
        viewModel.sendIntent(QuizSolveIntent.GetQuizBookLocalId(quizBookId))
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                QuizSolveEffect.NavigatePopBack -> {
                    navigateToBack()
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

    QuizSolveScreen(
        uiState = state,
        onIntent = viewModel::sendIntent
    )
}

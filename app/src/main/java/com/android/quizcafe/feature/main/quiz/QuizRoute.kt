package com.android.quizcafe.feature.main.quiz

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuizRoute(
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // side effect 처리
    LaunchedEffect(Unit) {
        viewModel.sendIntent(QuizIntent.FetchRecord)

        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is QuizEffect.ShowErrorDialog -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                QuizEffect.NavigateToOxQuiz -> {
                    // navigateToOxQuiz()
                }

                QuizEffect.NavigateToMultipleChoiceQuiz -> {
                    // navigateToMultipleChoiceQuiz()
                }

                QuizEffect.NavigateToShortAnswerQuiz -> {
                    // navigateToShortAnswerQuiz()
                }

                QuizEffect.NavigateToCreateQuiz -> {
                    // navigateToCreateQuiz()
                }
            }
        }
    }

    QuizScreen(
        state = state,
        onIntent = viewModel::sendIntent
    )
}

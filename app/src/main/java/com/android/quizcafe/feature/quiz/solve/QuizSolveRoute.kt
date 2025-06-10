package com.android.quizcafe.feature.quiz.solve

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuestionType
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveEffect
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveViewModel

@Composable
fun QuizSolveRoute(
    navigateToBack: () -> Unit,
    viewModel: QuizSolveViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

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
        uiState = state.copy(
            questionText = "Q1. 박승준의 나이는?",
            questionType = QuestionType.SUBJECTIVE,
            options = listOf(
                "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다."
            ),
            subjectHint = "니가 알아서 처 맞춰라"
        ),
        onIntent = viewModel::sendIntent
    )
}

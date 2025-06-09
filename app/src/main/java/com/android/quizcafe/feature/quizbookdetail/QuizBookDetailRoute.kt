package com.android.quizcafe.feature.quizbookdetail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R

@Composable
fun QuizBookDetailRoute(
    quizBookId: Long,
    navigateToQuizBookPicker: () -> Unit,
    navigateToQuizSolve: () -> Unit,
    navigateToUserPage: () -> Unit,
    viewModel: QuizBookDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(QuizBookDetailIntent.UpdateQuizBookId(quizBookId))
        viewModel.sendIntent(QuizBookDetailIntent.LoadQuizBookDetail) // API 요청
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                QuizBookDetailEffect.NavigateToQuizSolve -> {
                    navigateToQuizSolve()
                }

                QuizBookDetailEffect.NavigateToQuizBookList -> {
                    navigateToQuizBookPicker()
                }

                QuizBookDetailEffect.NavigateToUserPage -> {
                    navigateToUserPage()
                }

                is QuizBookDetailEffect.ShowError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    QuizBookDetailScreen(
        state = state,
        sendIntent = viewModel::sendIntent
    )
}

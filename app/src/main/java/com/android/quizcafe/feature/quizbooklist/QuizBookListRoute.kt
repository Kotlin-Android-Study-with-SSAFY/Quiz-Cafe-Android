package com.android.quizcafe.feature.quizbooklist

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R

@Composable
fun QuizBookListRoute(
    category: String,
    navigateToQuizBookDetail: (Long) -> Unit,
    navigateToCategory: () -> Unit,
    viewModel: QuizBookListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(QuizBookListIntent.UpdateCategory(category))
        viewModel.sendIntent(QuizBookListIntent.LoadQuizBooks) // API 요청
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                QuizBookListEffect.NavigateToCategory -> {
                    navigateToCategory()
                }

                is QuizBookListEffect.NavigateToQuizBookDetail -> {
                    navigateToQuizBookDetail(effect.quizBookId)
                }

                is QuizBookListEffect.ShowError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    QuizBookListScreen(
        state = state,
        sendIntent = viewModel::sendIntent
    )
}

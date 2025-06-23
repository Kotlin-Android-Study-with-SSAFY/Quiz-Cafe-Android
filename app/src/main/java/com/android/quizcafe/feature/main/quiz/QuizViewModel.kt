package com.android.quizcafe.feature.main.quiz

import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor() : BaseViewModel<QuizUiState, QuizIntent, QuizEffect>(
    initialState = QuizUiState()
) {

    override suspend fun handleIntent(intent: QuizIntent) {
        when (intent) {
            is QuizIntent.ClickQuizCard -> emitEffect(QuizEffect.NavigateToCategory(intent.quizType))
            is QuizIntent.LoadHistory -> {}
        }
    }

    override fun reduce(state: QuizUiState, intent: QuizIntent): QuizUiState {
        return when (intent) {
            is QuizIntent.LoadHistory -> state.copy(historyList = intent.histories)
            is QuizIntent.ClickQuizCard -> state
        }
    }
}

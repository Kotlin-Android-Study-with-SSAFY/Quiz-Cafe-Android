package com.android.quizcafe.feature.main.quiz

import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor() : BaseViewModel<QuizViewState, QuizIntent, QuizEffect>(
    initialState = QuizViewState()
) {

    override suspend fun handleIntent(intent: QuizIntent) {
        when(intent) {
            is QuizIntent.ClickQuizCard -> emitEffect(QuizEffect.NavigateToCategory(intent.quizType))
            is QuizIntent.LoadHistory -> {}
        }
    }

    override fun reduce(state: QuizViewState, intent: QuizIntent): QuizViewState {
        return when (intent) {
            is QuizIntent.LoadHistory -> state.copy(historyList = intent.histories)
            is QuizIntent.ClickQuizCard -> state
        }
    }
}

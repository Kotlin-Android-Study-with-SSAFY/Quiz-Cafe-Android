package com.android.quizcafe.feature.main.quiz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(QuizViewState())
    val state: StateFlow<QuizViewState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuizEffect>()
    val effect: SharedFlow<QuizEffect> = _effect.asSharedFlow()

    fun onIntent(intent: QuizIntent) {
        _state.value = reduce(_state.value, intent)
    }

    private fun reduce(state: QuizViewState, intent: QuizIntent): QuizViewState {
        return when (intent) {
            is QuizIntent.LoadHistory -> state.copy(historyList = intent.histories)
        }
    }
}

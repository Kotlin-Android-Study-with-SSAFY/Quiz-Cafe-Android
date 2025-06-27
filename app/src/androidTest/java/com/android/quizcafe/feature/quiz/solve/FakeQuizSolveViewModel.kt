package com.android.quizcafe.feature.quiz.solve

import com.android.quizcafe.feature.quiz.solve.viewmodel.IQuizSolveViewModel
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveEffect
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveIntent
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeQuizSolveViewModel : IQuizSolveViewModel {
    private val _state = MutableStateFlow(QuizSolveUiState())
    override val state: StateFlow<QuizSolveUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuizSolveEffect>()
    override val effect: SharedFlow<QuizSolveEffect> = _effect.asSharedFlow()

    val receivedIntents = mutableListOf<QuizSolveIntent>()

    override fun sendIntent(intent: QuizSolveIntent) {
        receivedIntents.add(intent)
    }

    // 테스트 편의를 위한 함수들 (인터페이스에는 포함되지 않음)
    suspend fun emitEffect(effect: QuizSolveEffect) {
        _effect.emit(effect)
    }

    fun setState(newState: QuizSolveUiState) {
        _state.value = newState
    }
}

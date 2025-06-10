package com.android.quizcafe.feature.quiz.solve.viewmodel

import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.ui.base.BaseViewModel
import com.android.quizcafe.feature.quiz.solve.component.AnswerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizSolveViewModel @Inject constructor() : BaseViewModel<QuizSolveUiState, QuizSolveIntent, QuizSolveEffect>(
    initialState = QuizSolveUiState()
) {
    init {
        viewModelScope.launch {
            while (true) {
                delay(1_000L)
                sendIntent(QuizSolveIntent.TickTime)
            }
        }
    }

    override suspend fun handleIntent(intent: QuizSolveIntent) {
        when (intent) {
            QuizSolveIntent.OnBackClick -> {
                emitEffect(QuizSolveEffect.NavigatePopBack)
            }

            else -> Unit
        }
    }

    override fun reduce(currentState: QuizSolveUiState, intent: QuizSolveIntent): QuizSolveUiState {
        return when (intent) {
            QuizSolveIntent.TickTime -> {
                when (currentState.playMode) {
                    PlayMode.TIME_ATTACK -> {
                        if (currentState.remainingSeconds > 0) {
                            currentState.copy(remainingSeconds = currentState.remainingSeconds - 1)
                        } else {
                            currentState
                        }
                    }

                    PlayMode.NO_TIME_ATTACK -> {
                        currentState.copy(elapsedSeconds = currentState.elapsedSeconds + 1)
                    }
                }
            }

            QuizSolveIntent.OnBackClick -> currentState

            is QuizSolveIntent.SelectOption -> {
                currentState.copy(
                    selectedOption = intent.option,
                    answerState = AnswerState.SELECTED,
                    isButtonEnabled = true
                )
            }

            is QuizSolveIntent.UpdatedSubjectiveAnswer ->
                currentState.copy(
                    selectedOption = intent.answer,
                    answerState = AnswerState.SELECTED,
                    isButtonEnabled = intent.answer.isNotBlank()
                )

            QuizSolveIntent.SubmitAnswer -> currentState
        }
    }
}

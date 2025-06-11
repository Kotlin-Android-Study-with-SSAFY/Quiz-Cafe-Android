package com.android.quizcafe.feature.quiz.solve.viewmodel

import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizSolveViewModel @Inject constructor(
    /*
     * 1. 문제집 정보 가져오는 UseCase -> 총 퀴즈수랑 각각의 퀴즈 id 가져올 수 있게
     * 2. 퀴즈id를 통한 문제 정보(객관식은 보기까지) 가져오는 UseCase
     * 3. 문제 채점 UseCase(아직 API가 없는듯?)
     * 4. 다음문제로 넘어갈때 로컬?혹은 서버로 현재 문제 state 저장하는 UseCase
     * */
) : BaseViewModel<QuizSolveUiState, QuizSolveIntent, QuizSolveEffect>(
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

            is QuizSolveIntent.SelectOption -> {
                currentState.copy(
                    selectedOptionId = intent.option,
                    isButtonEnabled = true
                )
            }

            is QuizSolveIntent.UpdatedSubjectiveAnswer ->
                currentState.copy(
                    subjectiveAnswer = intent.answer,
                    isButtonEnabled = intent.answer.isNotBlank()
                )
            QuizSolveIntent.ShowExplanation -> {
                currentState.copy(showExplanation = true)
            }
            else -> currentState
        }
    }
}

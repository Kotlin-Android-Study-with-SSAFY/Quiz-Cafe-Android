package com.android.quizcafe.feature.quiz.solvingResult

import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.usecase.solving.GetQuizBookSolvingUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizBookSolvingResultViewModel @Inject constructor(
    private val getQuizBookSolvingUseCase: GetQuizBookSolvingUseCase,
) : BaseViewModel<QuizBookSolvingResultUiState, QuizBookSolvingResultIntent, QuizBookSolvingResultEffect>(
    initialState = QuizBookSolvingResultUiState()
) {
    companion object {
        private const val TAG = "QuizBookSolvingResultViewModel"
    }

    override suspend fun handleIntent(intent: QuizBookSolvingResultIntent) {
        when (intent) {
            is QuizBookSolvingResultIntent.InitWithQuizBookGradeId -> {
                getQuizBookSolving(QuizBookGradeServerId(intent.quizBookGradeServerId))
            }
            QuizBookSolvingResultIntent.ClickFinish -> {
                emitEffect(QuizBookSolvingResultEffect.NavigateToMain)
            }
            is QuizBookSolvingResultIntent.Error -> {
                intent.errorMessage?.let { message ->
                    emitEffect(QuizBookSolvingResultEffect.ShowError(message))
                }
            }
            else -> Unit
        }
    }

    override fun reduce(
        state: QuizBookSolvingResultUiState,
        intent: QuizBookSolvingResultIntent
    ): QuizBookSolvingResultUiState = when (intent) {
        QuizBookSolvingResultIntent.StartLoading -> {
            state.copy(isLoading = true, errorMessage = null)
        }
        is QuizBookSolvingResultIntent.SuccessLoadData -> {
            state.copy(
                quizBookSolving = intent.quizBookSolving,
                isLoading = false,
                errorMessage = null
            )
        }
        is QuizBookSolvingResultIntent.Error -> {
            state.copy(
                isLoading = false,
                errorMessage = intent.errorMessage
            )
        }
        else -> state
    }

    private fun getQuizBookSolving(quizBookGradeServerId: QuizBookGradeServerId) = viewModelScope.launch {
        sendIntent(QuizBookSolvingResultIntent.StartLoading)

        getQuizBookSolvingUseCase(quizBookGradeServerId)
            .collect { gradeResource ->
                when (gradeResource) {
                    is Resource.Success -> {
                        sendIntent(QuizBookSolvingResultIntent.SuccessLoadData(gradeResource.data))
                    }
                    is Resource.Failure -> {
                        sendIntent(QuizBookSolvingResultIntent.Error(gradeResource.errorMessage))
                    }
                    is Resource.Loading -> Unit
                }
            }
    }
}

package com.android.quizcafe.feature.main.quiz

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.quizsolvingrecord.GetQuizRecordUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuizRecordUseCase: GetQuizRecordUseCase
) : BaseViewModel<QuizViewState, QuizIntent, QuizEffect>(
    initialState = QuizViewState()
) {
    override suspend fun handleIntent(intent: QuizIntent) {
        when (intent) {
            QuizIntent.FetchRecord -> {
                sendIntent(QuizIntent.LoadingFetchRecord)
                getQuizRecordUseCase().collect { resource ->
                    when (resource) {
                        is Resource.Success -> sendIntent(QuizIntent.SuccessFetchRecord(resource.data))
                        is Resource.Failure -> sendIntent(QuizIntent.FailFetchRecord(resource.errorMessage))
                        is Resource.Loading -> {}
                    }
                }
            }
            is QuizIntent.LoadingFetchRecord -> {}
            is QuizIntent.SuccessFetchRecord -> {}
            is QuizIntent.FailFetchRecord -> {
                emitEffect(QuizEffect.ShowErrorDialog(intent.errorMessage ?: "기록 불러오기 실패"))
            }
            QuizIntent.ClickOxQuiz -> emitEffect(QuizEffect.NavigateToOxQuiz)
            QuizIntent.ClickMultipleChoiceQuiz -> emitEffect(QuizEffect.NavigateToMultipleChoiceQuiz)
            QuizIntent.ClickShortAnswerQuiz -> emitEffect(QuizEffect.NavigateToShortAnswerQuiz)
            QuizIntent.ClickCreateQuiz -> emitEffect(QuizEffect.NavigateToCreateQuiz)
        }
    }

    override fun reduce(currentState: QuizViewState, intent: QuizIntent): QuizViewState {
        return when (intent) {
            QuizIntent.FetchRecord,
            is QuizIntent.LoadingFetchRecord -> currentState.copy(isLoading = true, errorMessage = null)
            is QuizIntent.SuccessFetchRecord -> currentState.copy(
                isLoading = false,
                quizSolvingRecords = intent.quizSolvingRecords,
                errorMessage = null
            )
            is QuizIntent.FailFetchRecord -> currentState.copy(
                isLoading = false,
                errorMessage = intent.errorMessage
            )
            else -> currentState
        }
    }
}

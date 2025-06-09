package com.android.quizcafe.feature.main.quiz

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.quiz.GetQuizHistoryUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuizHistoryUseCase: GetQuizHistoryUseCase
) : BaseViewModel<QuizViewState, QuizIntent, QuizEffect>(
    initialState = QuizViewState()
) {

    override suspend fun handleIntent(intent: QuizIntent) {
        when (intent) {
            QuizIntent.FetchRecord -> {
                // 로딩 시작
                sendIntent(QuizIntent.LoadingFetchRecord)
                getQuizHistoryUseCase().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            sendIntent(QuizIntent.SuccessFetchRecord(resource.data ?: emptyList()))
                        }

                        is Resource.Failure -> {
                            sendIntent(QuizIntent.FailFetchRecord(resource.errorMessage))
                        }

                        /* 필요 시 처리 */
                        is Resource.Loading -> {

                        }
                    }
                }
            }

            // 로딩 상태 진입
            // 상태만 변경, 별도 처리 필요 X
            is QuizIntent.LoadingFetchRecord -> {

            }

            // 성공적으로 히스토리 불러옴
            // 필요하면 효과도 emitEffect로 보낼 수 있음
            is QuizIntent.SuccessFetchRecord -> {

            }

            is QuizIntent.FailFetchRecord -> {
                emitEffect(QuizEffect.ShowErrorDialog(intent.errorMessage ?: "히스토리 불러오기에 실패했습니다."))
            }

            QuizIntent.ClickOxQuiz -> emitEffect(QuizEffect.NavigateToOxQuiz)
            QuizIntent.ClickMultipleChoiceQuiz -> emitEffect(QuizEffect.NavigateToMultipleChoiceQuiz)
            QuizIntent.ClickShortAnswerQuiz -> emitEffect(QuizEffect.NavigateToShortAnswerQuiz)
            QuizIntent.ClickCreateQuiz -> emitEffect(QuizEffect.NavigateToCreateQuiz)
        }
    }

    override fun reduce(currentState: QuizViewState, intent: QuizIntent): QuizViewState {
        return when (intent) {
            QuizIntent.FetchRecord -> currentState.copy(isLoading = true, errorMessage = null)
            is QuizIntent.LoadingFetchRecord -> currentState.copy(isLoading = true, errorMessage = null)
            is QuizIntent.SuccessFetchRecord -> currentState.copy(
                isLoading = false,
                quizRecords = intent.quizRecords,
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

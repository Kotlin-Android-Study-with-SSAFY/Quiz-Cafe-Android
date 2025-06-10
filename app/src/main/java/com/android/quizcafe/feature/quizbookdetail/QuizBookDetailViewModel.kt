package com.android.quizcafe.feature.quizbookdetail

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookDetailRequest
import com.android.quizcafe.core.domain.usecase.quizbook.GetQuizBookDetailUseCase
import com.android.quizcafe.core.domain.usecase.quizbook.MarkQuizBookUseCase
import com.android.quizcafe.core.domain.usecase.quizbook.UnmarkQuizBookUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizBookDetailViewModel @Inject constructor(
    private val getQuizBookDetailUseCase: GetQuizBookDetailUseCase,
    private val markQuizBookUseCase: MarkQuizBookUseCase,
    private val unmarkQuizBookUseCase: UnmarkQuizBookUseCase
) : BaseViewModel<QuizBookDetailViewState, QuizBookDetailIntent, QuizBookDetailEffect>(
    initialState = QuizBookDetailViewState()
) {

    override suspend fun handleIntent(intent: QuizBookDetailIntent) {
        when (intent) {
            QuizBookDetailIntent.ClickQuizSolve -> emitEffect(QuizBookDetailEffect.NavigateToQuizSolve)
            QuizBookDetailIntent.ClickMarkQuizBook -> markQuizBook()
            QuizBookDetailIntent.ClickUnmarkQuizBook -> unmarkQuizBook()
            QuizBookDetailIntent.ClickUser -> emitEffect(QuizBookDetailEffect.NavigateToUserPage)
            QuizBookDetailIntent.LoadQuizBookDetail -> getQuizBookDetail()
            is QuizBookDetailIntent.SuccessGetQuizBookDetail -> {}
            QuizBookDetailIntent.SuccessMarkQuizBook -> {}
            QuizBookDetailIntent.SuccessUnmarkQuizBook -> {}
            is QuizBookDetailIntent.UpdateQuizBookId -> {}
            is QuizBookDetailIntent.FailGetQuizBookDetail -> emitEffect(QuizBookDetailEffect.ShowError(intent.errorMessage ?: ""))
            is QuizBookDetailIntent.FailUpdateSaveState -> emitEffect(QuizBookDetailEffect.ShowError(intent.errorMessage ?: ""))
        }
    }

    override fun reduce(currentState: QuizBookDetailViewState, intent: QuizBookDetailIntent): QuizBookDetailViewState {
        return when (intent) {
            QuizBookDetailIntent.LoadQuizBookDetail -> currentState.copy(isLoading = true, errorMessage = null)
            QuizBookDetailIntent.ClickQuizSolve -> currentState.copy(isLoading = true, errorMessage = null)
            QuizBookDetailIntent.ClickMarkQuizBook -> currentState.copy(isLoading = true, errorMessage = null)
            QuizBookDetailIntent.ClickUnmarkQuizBook -> currentState.copy(isLoading = true, errorMessage = null)
            QuizBookDetailIntent.ClickUser -> currentState.copy(isLoading = true, errorMessage = null)

            is QuizBookDetailIntent.UpdateQuizBookId -> currentState.copy(quizBookId = intent.quizBookId)

            is QuizBookDetailIntent.SuccessGetQuizBookDetail -> currentState.copy(
                quizBookDetail = intent.data,
                isLoading = false
            )

            QuizBookDetailIntent.SuccessMarkQuizBook -> {
                currentState.copy(isLoading = false, errorMessage = null, quizBookDetail = state.value.quizBookDetail.copy(isSaved = true))
            }

            QuizBookDetailIntent.SuccessUnmarkQuizBook -> {
                currentState.copy(isLoading = false, errorMessage = null, quizBookDetail = state.value.quizBookDetail.copy(isSaved = false))
            }

            is QuizBookDetailIntent.FailGetQuizBookDetail -> currentState.copy(isLoading = false, errorMessage = "로그인에 실패했습니다.")
            is QuizBookDetailIntent.FailUpdateSaveState -> currentState.copy(isLoading = false, errorMessage = null)
        }
    }

    private suspend fun getQuizBookDetail() {
        getQuizBookDetailUseCase(
            QuizBookDetailRequest(state.value.quizBookId)
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("quizBookDetail", "Get QuizBookDetail Success")
                    sendIntent(QuizBookDetailIntent.SuccessGetQuizBookDetail(it.data))
                }

                is Resource.Loading -> {
                    Log.d("quizBookDetail", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("quizBookDetail", "${it.errorMessage}")
                    sendIntent(QuizBookDetailIntent.FailGetQuizBookDetail(it.errorMessage))
                }
            }
        }
    }

    private suspend fun markQuizBook() {
        markQuizBookUseCase(state.value.quizBookId).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("quizBookDetail", "Save QuizBook Success")
                    sendIntent(QuizBookDetailIntent.SuccessMarkQuizBook)
                    sendIntent(QuizBookDetailIntent.LoadQuizBookDetail)
                }

                is Resource.Loading -> {
                    Log.d("quizBookDetail", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("quizBookDetail", "Save QuizBook Fail")
                    sendIntent(QuizBookDetailIntent.FailUpdateSaveState(it.errorMessage))
                }
            }
        }
    }

    private suspend fun unmarkQuizBook() {
        unmarkQuizBookUseCase(state.value.quizBookId).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("quizBookDetail", "Unsave QuizBook Success")
                    sendIntent(QuizBookDetailIntent.SuccessUnmarkQuizBook)
                    sendIntent(QuizBookDetailIntent.LoadQuizBookDetail)
                }

                is Resource.Loading -> {
                    Log.d("quizBookDetail", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("quizBookDetail", it.errorMessage)
                    sendIntent(QuizBookDetailIntent.FailUpdateSaveState(it.errorMessage))
                }
            }
        }
    }
}

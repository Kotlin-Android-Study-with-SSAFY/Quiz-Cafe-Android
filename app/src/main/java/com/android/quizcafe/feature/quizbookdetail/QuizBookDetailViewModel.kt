package com.android.quizcafe.feature.quizbookdetail

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookDetailRequest
import com.android.quizcafe.core.domain.usecase.quizbook.GetQuizBookDetailUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizBookDetailViewModel @Inject constructor(
    private val getQuizBookDetailUseCase: GetQuizBookDetailUseCase
) : BaseViewModel<QuizBookDetailViewState, QuizBookDetailIntent, QuizBookDetailEffect>(
    initialState = QuizBookDetailViewState()
) {

    override suspend fun handleIntent(intent: QuizBookDetailIntent) {
        when (intent) {
            QuizBookDetailIntent.ClickQuizSolve -> emitEffect(QuizBookDetailEffect.NavigateToQuizSolve)
            QuizBookDetailIntent.ClickSaveQuizBook -> {}
            QuizBookDetailIntent.ClickUser -> emitEffect(QuizBookDetailEffect.NavigateToUserPage)
            is QuizBookDetailIntent.FailGetQuizBookDetail -> emitEffect(QuizBookDetailEffect.ShowError(intent.errorMessage ?: ""))
            QuizBookDetailIntent.LoadQuizBookDetail -> getQuizBookDetailList()
            is QuizBookDetailIntent.SuccessGetQuizBookDetail -> {}
        }
    }

    override fun reduce(currentState: QuizBookDetailViewState, intent: QuizBookDetailIntent): QuizBookDetailViewState {
        return when (intent) {
            QuizBookDetailIntent.LoadQuizBookDetail -> currentState.copy(isLoading = true, errorMessage = null)
            QuizBookDetailIntent.ClickQuizSolve -> currentState.copy(isLoading = true, errorMessage = null)
            QuizBookDetailIntent.ClickSaveQuizBook -> currentState.copy(isLoading = true, errorMessage = null)
            QuizBookDetailIntent.ClickUser -> currentState.copy(isLoading = true, errorMessage = null)
            is QuizBookDetailIntent.SuccessGetQuizBookDetail -> currentState.copy(
                quizBookDetail = intent.data,
                isLoading = false
            )
            is QuizBookDetailIntent.FailGetQuizBookDetail -> currentState.copy(isLoading = false, errorMessage = "로그인에 실패했습니다.")

        }
    }

    private suspend fun QuizBookDetailViewModel.getQuizBookDetailList() {
        getQuizBookDetailUseCase(
            QuizBookDetailRequest(state.value.quizBookId)
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("quizBookDetail", "Get QuizBookDetail List Success")
                    sendIntent(QuizBookDetailIntent.SuccessGetQuizBookDetail(it.data))
                }

                is Resource.Loading -> {
                    Log.d("quizBookDetail", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("quizBookDetail", "Get QuizBookDetail List Fail")
                    sendIntent(QuizBookDetailIntent.FailGetQuizBookDetail(it.errorMessage))
                }
            }
        }
    }
}

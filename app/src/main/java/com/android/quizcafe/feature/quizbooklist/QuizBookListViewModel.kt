package com.android.quizcafe.feature.quizbooklist

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookRequest
import com.android.quizcafe.core.domain.usecase.quizbook.GetQuizBookListUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizBookListViewModel @Inject constructor(
    private val getQuizBookListUseCase: GetQuizBookListUseCase
) : BaseViewModel<QuizBookListViewState, QuizBookListIntent, QuizBookListEffect>(
    initialState = QuizBookListViewState()
) {

    override suspend fun handleIntent(intent: QuizBookListIntent) {
        when (intent) {
            QuizBookListIntent.LoadQuizBooks -> getQuizBookList()
            QuizBookListIntent.ClickQuizBookList -> emitEffect(QuizBookListEffect.NavigateToQuizBookDetail)
            is QuizBookListIntent.SuccessGetQuizBooks -> {}
            is QuizBookListIntent.FailGetQuizBooks -> emitEffect(QuizBookListEffect.ShowError(intent.errorMessage ?: ""))
            is QuizBookListIntent.UpdateCategory -> {}
        }
    }

    override fun reduce(currentState: QuizBookListViewState, intent: QuizBookListIntent): QuizBookListViewState {
        return when (intent) {
            QuizBookListIntent.LoadQuizBooks -> currentState.copy(isLoading = true, errorMessage = null)
            QuizBookListIntent.ClickQuizBookList -> currentState.copy(isLoading = true, errorMessage = null)
            is QuizBookListIntent.UpdateCategory -> currentState.copy(category = intent.category)
            is QuizBookListIntent.SuccessGetQuizBooks -> currentState.copy(
                quizBooks = intent.data,
                isLoading = false
            )
            is QuizBookListIntent.FailGetQuizBooks -> currentState.copy(isLoading = false, errorMessage = "로그인에 실패했습니다.")
        }
    }

    private suspend fun QuizBookListViewModel.getQuizBookList() {
        getQuizBookListUseCase(
            QuizBookRequest(category = state.value.category)
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("quizBookList", "Get QuizBookList List Success")
                    sendIntent(QuizBookListIntent.SuccessGetQuizBooks(it.data))
                }

                is Resource.Loading -> {
                    Log.d("quizBookList", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("quizBookList", "Get QuizBookList List Fail")
                    sendIntent(QuizBookListIntent.FailGetQuizBooks(it.errorMessage))
                }
            }
        }
    }
}

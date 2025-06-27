package com.android.quizcafe.feature.main.home

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.quizbook.GetLatestQuizBookUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLatestQuizBookUseCase: GetLatestQuizBookUseCase
) : BaseViewModel<HomeUiState, HomeIntent, HomeEffect>(
    initialState = HomeUiState()
) {
    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.FetchLatestQuizBook -> {
                sendIntent(HomeIntent.LoadingLatestQuizBook)
                getLatestQuizBookUseCase().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            sendIntent(HomeIntent.SuccessFetchLatestQuizBook(resource.data))
                            Log.d("HomeScreen", resource.data.toString())
                        }

                        is Resource.Failure -> {
                            sendIntent(HomeIntent.FailFetchLatestQuizBook(resource.errorMessage))
                            Log.d("HomeScreen", resource.errorMessage)
                        }

                        else -> Unit
                    }
                }
            }

            is HomeIntent.FailFetchLatestQuizBook -> {
                emitEffect(HomeEffect.ShowErrorDialog(intent.errorMessage ?: "최신 문제집 불러오기 실패"))
            }

            is HomeIntent.ClickHomeCard -> emitEffect(HomeEffect.NavigateToCategory(intent.quizType))
            else -> Unit
        }
    }

    override fun reduce(currentState: HomeUiState, intent: HomeIntent): HomeUiState {
        return when (intent) {
            HomeIntent.FetchLatestQuizBook,
            is HomeIntent.LoadingLatestQuizBook -> currentState.copy(isLoading = true, errorMessage = null)

            is HomeIntent.SuccessFetchLatestQuizBook -> currentState.copy(
                isLoading = false,
                latestQuizBooks = intent.latestQuizBooks,
                errorMessage = null
            )

            is HomeIntent.FailFetchLatestQuizBook -> currentState.copy(
                isLoading = false,
                errorMessage = intent.errorMessage
            )

            is HomeIntent.ClickHomeCard -> currentState
        }
    }
}

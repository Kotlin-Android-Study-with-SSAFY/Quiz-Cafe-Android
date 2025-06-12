package com.android.quizcafe.feature.main.home

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.solving.GetAllQuizBookSolvingUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllQuizBookSolvingUseCase: GetAllQuizBookSolvingUseCase
) : BaseViewModel<HomeUiState, HomeIntent, HomeEffect>(
    initialState = HomeUiState()
) {
    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.FetchRecord -> {
                sendIntent(HomeIntent.LoadingFetchRecord)
                getAllQuizBookSolvingUseCase().collect { resource ->
                    when (resource) {
                        is Resource.Success -> sendIntent(HomeIntent.SuccessFetchRecord(resource.data))
                        is Resource.Failure -> sendIntent(HomeIntent.FailFetchRecord(resource.errorMessage))
                        else -> Unit
                    }
                }
            }
            is HomeIntent.FailFetchRecord -> {
                emitEffect(HomeEffect.ShowErrorDialog(intent.errorMessage ?: "기록 불러오기 실패"))
            }
            is HomeIntent.ClickHomeCard -> emitEffect(HomeEffect.NavigateToCategory(intent.quizType))
            else -> Unit
        }
    }

    override fun reduce(currentState: HomeUiState, intent: HomeIntent): HomeUiState {
        return when (intent) {
            HomeIntent.FetchRecord,
            is HomeIntent.LoadingFetchRecord -> currentState.copy(isLoading = true, errorMessage = null)
            is HomeIntent.SuccessFetchRecord -> currentState.copy(
                isLoading = false,
                quizBookSolvings = intent.quizBookSolvings,
                errorMessage = null
            )
            is HomeIntent.FailFetchRecord -> currentState.copy(
                isLoading = false,
                errorMessage = intent.errorMessage
            )
            is HomeIntent.ClickHomeCard -> currentState
        }
    }
}

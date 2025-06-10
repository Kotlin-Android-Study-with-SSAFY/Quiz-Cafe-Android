package com.android.quizcafe.feature.categorypicker

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.request.CategoryRequest
import com.android.quizcafe.core.domain.usecase.quizbook.GetCategoryListUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryListUseCase
) : BaseViewModel<CategoryUiState, CategoryIntent, CategoryEffect>(
    initialState = CategoryUiState()
) {

    override suspend fun handleIntent(intent: CategoryIntent) {
        when (intent) {
            CategoryIntent.LoadCategories -> getCategoryList()
            is CategoryIntent.ClickCategory -> emitEffect(CategoryEffect.NavigateToQuizBooks(intent.categoryId))
            is CategoryIntent.SuccessGetCategories -> {}
            is CategoryIntent.FailGetCategories -> emitEffect(CategoryEffect.ShowError(intent.errorMessage ?: ""))
        }
    }

    override fun reduce(currentState: CategoryUiState, intent: CategoryIntent): CategoryUiState {
        return when (intent) {
            CategoryIntent.LoadCategories -> currentState.copy(isLoading = true, errorMessage = null)
            is CategoryIntent.ClickCategory -> currentState.copy(isLoading = true, errorMessage = null)
            is CategoryIntent.SuccessGetCategories -> currentState.copy(categories = intent.data, isLoading = false)
            is CategoryIntent.FailGetCategories -> currentState.copy(isLoading = false, errorMessage = "로그인에 실패했습니다.")
        }
    }

    private suspend fun CategoryViewModel.getCategoryList() {
        getCategoryUseCase(
            CategoryRequest(quizType = state.value.quizType)
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("category", "Get Category List Success")
                    sendIntent(CategoryIntent.SuccessGetCategories(it.data))
                }

                is Resource.Loading -> {
                    Log.d("category", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("category", "Get Category List Fail")
                    sendIntent(CategoryIntent.FailGetCategories(it.errorMessage))
                }
            }
        }
    }
}

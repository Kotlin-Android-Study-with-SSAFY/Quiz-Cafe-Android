package com.android.quizcafe.feature.categorypicker

import com.android.quizcafe.core.domain.model.quizbook.response.Category
import com.android.quizcafe.core.ui.base.BaseContract

data class CategoryUiState(
    val quizType: String = "",
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : BaseContract.UiState

sealed class CategoryIntent : BaseContract.ViewIntent {
    data object LoadCategories : CategoryIntent()
    data class ClickCategory(val categoryId: String) : CategoryIntent()

    data class SuccessGetCategories(val data: List<Category>) : CategoryIntent()
    data class FailGetCategories(val errorMessage: String? = null) : CategoryIntent()
}

sealed class CategoryEffect : BaseContract.ViewEffect {
    data class ShowError(val message: String) : CategoryEffect()
    data object NavigateToHome : CategoryEffect()
    data class NavigateToQuizBooks(val categoryId: String) : CategoryEffect()
}

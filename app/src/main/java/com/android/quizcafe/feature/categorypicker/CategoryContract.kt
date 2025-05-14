package com.android.quizcafe.feature.categorylist

import com.android.quizcafe.core.domain.model.quizbook.response.Category
import com.android.quizcafe.core.ui.base.BaseContract

data class CategoryViewState(
    val quizType: String = "",
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : BaseContract.ViewState

sealed class CategoryIntent : BaseContract.ViewIntent {
    data object LoadCategories : CategoryIntent()
    data object ClickCategory : CategoryIntent()

    data class SuccessGetCategories(val data: List<Category>) : CategoryIntent()
    data class FailGetCategories(val errorMessage: String? = null) : CategoryIntent()
}

sealed class CategoryEffect : BaseContract.ViewEffect {
    data class ShowError(val message: String) : CategoryEffect()
    data object NavigateToHome : CategoryEffect()
    data object NavigateToQuizBooks : CategoryEffect()
}

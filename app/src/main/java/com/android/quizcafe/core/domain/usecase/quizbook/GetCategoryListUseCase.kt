package com.android.quizcafe.core.domain.usecase.quizbook

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.reponse.Category
import com.android.quizcafe.core.domain.model.quizbook.request.CategoryRequest
import com.android.quizcafe.core.domain.repository.QuizBookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    private val quizBookRepository: QuizBookRepository
) {
    operator fun invoke(categoryRequest: CategoryRequest): Flow<Resource<List<Category>>> =
        quizBookRepository.getAllCategories(categoryRequest)
}

package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.model.quizbook.response.CategoryResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.toDomain
import com.android.quizcafe.core.data.model.quizbook.request.toDto
import com.android.quizcafe.core.data.remote.datasource.QuizBookRemoteDataSource
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.response.Category
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.request.CategoryRequest
import com.android.quizcafe.core.domain.repository.QuizBookRepository
import com.android.quizcafe.core.network.mapper.apiResponseToResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuizBookRepositoryImpl @Inject constructor(
    private val quizBookRemoteDataSource: QuizBookRemoteDataSource
) : QuizBookRepository {

    override fun getAllCategories(categoryRequest: CategoryRequest): Flow<Resource<List<Category>>> =
        apiResponseToResourceFlow(mapper = CategoryResponseDto::toDomain) {
            quizBookRemoteDataSource.getCategoriesByType(categoryRequest.toDto())
        }

    override fun getQuizBooksByCategory(categoryId: String): Flow<Resource<List<QuizBook>>> {
        TODO("Not yet implemented")
    }

    override fun getQuizBookById(id: Long): Flow<Resource<QuizBookDetail>> {
        TODO("Not yet implemented")
    }
}

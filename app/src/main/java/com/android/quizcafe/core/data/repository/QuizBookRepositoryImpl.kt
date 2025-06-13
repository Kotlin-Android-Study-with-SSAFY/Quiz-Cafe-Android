package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.mapper.quizbook.toDomain
import com.android.quizcafe.core.data.model.quizbook.request.toDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookCategoryResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.toDomain
import com.android.quizcafe.core.data.remote.datasource.QuizBookRemoteDataSource
import com.android.quizcafe.core.database.dao.quizBook.QuizBookDao
import com.android.quizcafe.core.database.dao.quiz.QuizDao
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookDetailRequest
import com.android.quizcafe.core.domain.model.quizbook.response.Category
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.request.CategoryRequest
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookRequest
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizBookRepository
import com.android.quizcafe.core.network.mapper.apiResponseListToResourceFlow
import com.android.quizcafe.core.network.mapper.apiResponseToResourceFlow
import com.android.quizcafe.core.network.mapper.noContentResponseToResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuizBookRepositoryImpl @Inject constructor(
    private val quizDao: QuizDao,
    private val quizBookDao: QuizBookDao,
    private val quizBookRemoteDataSource: QuizBookRemoteDataSource
) : QuizBookRepository {

    override fun getAllCategories(categoryRequest: CategoryRequest): Flow<Resource<List<Category>>> =
        apiResponseListToResourceFlow(mapper = QuizBookCategoryResponseDto::toDomain) {
            quizBookRemoteDataSource.getCategoriesByType(categoryRequest.toDto())
        }

    /*
     TODO : 퀴즈북 정보 + 퀴즈 리스트 API 새로 생성되면 그거 호출
     TODO : 그 이후에 로컬에 퀴즈북 정보 저장
     TODO : 로컬에서 퀴즈북 with 퀴즈 리스트 가져와서 QuizSolve로 뿌려주기
     */

    override fun getQuizBookById(quizBookId : QuizBookId) : Flow<Resource<QuizBook>> =
        apiResponseToResourceFlow(mapper = { it.toDomain() }) {
            quizBookRemoteDataSource.getQuizBookWithQuizListById(quizBookId.value)
        }

    override fun getQuizBookListByCategory(quizBookRequest: QuizBookRequest): Flow<Resource<List<QuizBook>>> =
        apiResponseListToResourceFlow(mapper = { it.toDomain() }) {
            quizBookRemoteDataSource.getQuizBooksByCategory(quizBookRequest.toDto())
        }

    override fun getQuizBookDetailById(quizBookDetailRequest: QuizBookDetailRequest): Flow<Resource<QuizBookDetail>> =
        apiResponseToResourceFlow(mapper = { it.toDomain() }) {
            quizBookRemoteDataSource.getQuizBookDetail(quizBookDetailRequest.toDto())
        }

    override fun markQuizBook(quizBookId: Long): Flow<Resource<Unit>> =
        noContentResponseToResourceFlow { quizBookRemoteDataSource.markQuizBook(quizBookId) }

    override fun unmarkQuizBook(quizBookId: Long): Flow<Resource<Unit>> =
        noContentResponseToResourceFlow { quizBookRemoteDataSource.unmarkQuizBook(quizBookId) }
}

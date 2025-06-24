package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.data.mapper.quiz.toEntity
import com.android.quizcafe.core.data.mapper.quizbook.toDomain
import com.android.quizcafe.core.data.mapper.quizbook.toEntity
import com.android.quizcafe.core.data.model.quizbook.request.toDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookCategoryResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookWithQuizzesResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.toDomain
import com.android.quizcafe.core.data.remote.datasource.QuizBookRemoteDataSource
import com.android.quizcafe.core.data.util.LocalErrorCode
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
import com.android.quizcafe.core.network.model.onErrorOrException
import com.android.quizcafe.core.network.model.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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

    // 서버로부터 퀴즈북을 받아 로컬 우선 저장 후 로컬에서 가져오기
    override fun getQuizBookFromRemote(quizBookId: QuizBookId): Flow<Resource<QuizBook>> = flow {
        emit(Resource.Loading)
        quizBookRemoteDataSource.getQuizBookWithQuizListById(quizBookId.value)
            .onSuccess { response ->
                response.data?.let { quizBookDto ->
                    saveQuizBookToLocal(quizBookDto)
                    emit(Resource.Success(quizBookDto.toDomain()))
                } ?: emit(Resource.Failure("퀴즈북 데이터가 없습니다", HttpStatus.UNKNOWN))
            }
            .onErrorOrException { code, message ->
                emit(Resource.Failure(message ?: "퀴즈북 조회 실패", code))
            }
    }.catch { e ->
        emit(Resource.Failure("퀴즈북 조회 중 오류: ${e.message}", LocalErrorCode.ROOM_ERROR))
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

    // 서버 데이터를 로컬 DB에 저장
    private suspend fun saveQuizBookToLocal(quizBookDto: QuizBookWithQuizzesResponseDto) {
        quizBookDao.upsertQuizBook(quizBookDto.toEntity())

        val quizEntities = quizBookDto.quizzes.map { it.toEntity() }
        quizDao.upsertQuizList(quizEntities)

        val mcqOptionEntities = quizBookDto.quizzes.flatMap { quizDto ->
            quizDto.mcqOption.map { it.toEntity() }
        }
        if (mcqOptionEntities.isNotEmpty()) {
            quizDao.upsertMcqOptions(mcqOptionEntities)
        }
    }
}

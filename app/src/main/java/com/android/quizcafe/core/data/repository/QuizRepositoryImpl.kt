package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.mapper.solving.toDomain
import com.android.quizcafe.core.data.mapper.quiz.toEntity
import com.android.quizcafe.core.data.remote.datasource.QuizRemoteDataSource
import com.android.quizcafe.core.database.dao.quiz.QuizDao
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizRepository
import com.android.quizcafe.core.network.model.onErrorOrException
import com.android.quizcafe.core.network.model.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val quizDao: QuizDao,
    private val quizRemoteDataSource: QuizRemoteDataSource
) : QuizRepository {

    /**
     * 로컬 데이터 우선 반환,
     * 로컬 데이터 없을 경우 서버에 요청,
     * 서버에서 새로 받은 경우 로컬 업데이트
     */
    override fun getQuizListByBookId(quizBookId: QuizBookId): Flow<Resource<List<Quiz>>> = flow {
        emit(Resource.Loading)

        val localQuizList = quizDao.getQuizListWithOptionsByBookId(quizBookId.value)
        if (localQuizList.isNotEmpty()) {
            emit(Resource.Success(localQuizList.map { it.toDomain() }))
            return@flow
        }

        quizRemoteDataSource.getQuizListByBookId(quizBookId.value)
            .onSuccess { response ->
                response.data?.let { quizList ->
                    quizDao.upsertQuizList(
                        quizList = quizList.map { it.toEntity() }
                    )
                    val updatedLocal = quizDao.getQuizListWithOptionsByBookId(quizBookId.value)
                    emit(Resource.Success(updatedLocal.map { it.toDomain() }))
                } ?: emit(Resource.Failure.NullData)
            }
            .onErrorOrException { code, message ->
                emit(Resource.Failure(message ?: "알 수 없는 오류", code))
            }
    }
}

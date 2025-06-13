package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.toDomain
import com.android.quizcafe.core.data.model.quizsolvingrecord.response.toDomain
import com.android.quizcafe.core.data.model.user.request.toDto
import com.android.quizcafe.core.data.model.user.response.toDomain
import com.android.quizcafe.core.data.remote.datasource.QuizSolvingRecordRemoteDataSource
import com.android.quizcafe.core.data.remote.datasource.UserRemoteDataSource
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.user.requst.ResetPasswordRequest
import com.android.quizcafe.core.domain.model.user.response.UserInfo
import com.android.quizcafe.core.domain.repository.UserRepository
import com.android.quizcafe.core.network.mapper.apiResponseListToResourceFlow
import com.android.quizcafe.core.network.mapper.noContentResponseToResourceFlow
import com.android.quizcafe.core.network.model.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val quizSolvingRecordRemoteDataSource: QuizSolvingRecordRemoteDataSource,
) : UserRepository {

    override fun getUserInfo(): Flow<Resource<UserInfo>> = flow {
        emit(Resource.Loading)

        val userResult = userRemoteDataSource.getUserInfo()
        val recordResult = quizSolvingRecordRemoteDataSource.getAllQuizSolvingRecordsByUser()

        val userInfo = (userResult as? NetworkResult.Success)?.data?.data
        val recordDtoList = (recordResult as? NetworkResult.Success)?.data?.data

        if (userInfo != null && recordDtoList != null) {
            val records = recordDtoList.map { it.toDomain() }
            val quizCount = records.sumOf { it.quizzes.size }
            val quizBookCount = records.map { it.quizBookId }.distinct().count()
            val joinDateStr = userInfo.joinDateStr
            val quizSolvingRecord = records
                .asSequence()
                .flatMap { it.quizzes }
                .groupingBy { it.completedAt.take(10) }
                .eachCount()
                .toSortedMap()

            emit(
                Resource.Success(
                    userInfo.toDomain(
                        quizCount = quizCount,
                        quizBookCount = quizBookCount,
                        joinDateStr = joinDateStr,
                        quizSolvingRecord = quizSolvingRecord
                    )
                )
            )
        } else {
            val error = userResult as? NetworkResult.Error
                ?: recordResult as? NetworkResult.Error
            val errorMsg = error?.message ?: "유저 정보를 불러오지 못했습니다."
            val errorCode = error?.code ?: -1
            emit(Resource.Failure(errorMsg, errorCode))
        }
    }

    override fun updateUserNickName(nickName: String): Flow<Resource<Unit>> =
        noContentResponseToResourceFlow { userRemoteDataSource.updateUserNickName(nickName) }

    override fun resetPassword(request: ResetPasswordRequest): Flow<Resource<Unit>> =
        noContentResponseToResourceFlow { userRemoteDataSource.resetPassword(request.toDto()) }

    override fun deleteUser(): Flow<Resource<Unit>> =
        noContentResponseToResourceFlow { userRemoteDataSource.deleteUser() }

    override fun getMyQuizBooks(): Flow<Resource<List<QuizBook>>> =
        apiResponseListToResourceFlow(mapper = QuizBookResponseDto::toDomain) {
            userRemoteDataSource.getMyQuizBooks()
        }
}

package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.mapper.quizbook.toDomain
import com.android.quizcafe.core.data.mapper.solving.toDomain
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.model.user.request.toDto
import com.android.quizcafe.core.data.model.user.response.toDomain
import com.android.quizcafe.core.data.remote.datasource.QuizBookSolvingRemoteDataSource
import com.android.quizcafe.core.data.remote.datasource.UserRemoteDataSource
import com.android.quizcafe.core.datastore.AuthManager
import com.android.quizcafe.core.datastore.LogoutReason
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.user.request.UpdateNicknameRequest
import com.android.quizcafe.core.domain.model.user.request.UpdatePasswordRequest
import com.android.quizcafe.core.domain.model.user.response.UserInfo
import com.android.quizcafe.core.domain.repository.UserRepository
import com.android.quizcafe.core.network.mapper.apiResponseListToResourceFlow
import com.android.quizcafe.core.network.mapper.emptyApiResponseToResourceFlow
import com.android.quizcafe.core.network.mapper.noContentResponseToResourceFlow
import com.android.quizcafe.core.network.model.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val quizBookSolvingRemoteDataSource: QuizBookSolvingRemoteDataSource,
    private val authManager: AuthManager
) : UserRepository {

    override fun getUserInfo(): Flow<Resource<UserInfo>> = flow {
        emit(Resource.Loading)

        val userResult = userRemoteDataSource.getUserInfo()
        val recordResult = quizBookSolvingRemoteDataSource.getAllQuizBookSolvingByUser()

        val userInfo = (userResult as? NetworkResult.Success)?.data?.data
        val recordDtoList = (recordResult as? NetworkResult.Success)?.data?.data ?: emptyList()

        if (userInfo == null) {
            val error = userResult as? NetworkResult.Error ?: recordResult as? NetworkResult.Error
            emit(Resource.Failure(error?.message ?: "유저 정보를 불러오지 못했습니다.", error?.code ?: -1))
            return@flow
        }

        val records = recordDtoList.map { it.toDomain() }
        val quizCount = records.sumOf { it.quizSolvingList.size }
        val quizBookCount = records.map { it.quizBookId }.distinct().count()
        val quizSolvingRecord =
            records.asSequence().flatMap { it.quizSolvingList }.groupingBy { it.completedAt.take(10) }
                .eachCount().toSortedMap()

        emit(
            Resource.Success(
                userInfo.toDomain(
                    quizCount = quizCount,
                    quizBookCount = quizBookCount,
                    joinDateStr = userInfo.joinDateStr,
                    quizSolvingRecord = quizSolvingRecord
                )
            )
        )
    }

    override fun updateUserNickName(request: UpdateNicknameRequest): Flow<Resource<Unit>> =
        emptyApiResponseToResourceFlow { userRemoteDataSource.updateUserNickName(request.toDto()) }

    override fun deleteUser(): Flow<Resource<Unit>> = flow {
        noContentResponseToResourceFlow {
            userRemoteDataSource.deleteUser()
        }.collect { result ->
            emit(result)

            if (result is Resource.Success) {
                authManager.logout(LogoutReason.UserWithdrawal)
            }
        }
    }

    override fun getCreatedQuizBooksByMe(): Flow<Resource<List<QuizBook>>> =
        apiResponseListToResourceFlow(mapper = QuizBookResponseDto::toDomain) {
            userRemoteDataSource.getCreatedQuizBooksByMe()
        }

    override fun updatePassword(request: UpdatePasswordRequest): Flow<Resource<Unit>> = flow {
        emptyApiResponseToResourceFlow { userRemoteDataSource.updatePassword(request.toDto()) }
            .collect { result ->
                emit(result)

                if (result is Resource.Success) {
                    authManager.logout(LogoutReason.PasswordUpdated)
                }
            }
    }
}

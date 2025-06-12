package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.model.user.response.UserInfoResponseDto
import com.android.quizcafe.core.data.model.user.response.toDomain
import com.android.quizcafe.core.data.remote.datasource.UserRemoteDataSource
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.user.UserInfo
import com.android.quizcafe.core.domain.repository.QuizBookRepository
import com.android.quizcafe.core.domain.repository.UserRepository
import com.android.quizcafe.core.network.mapper.apiResponseToResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val quizBookRepository: QuizBookRepository
) : UserRepository {

    override fun getUserInfo(): Flow<Resource<UserInfo>> =
        apiResponseToResourceFlow(mapper = UserInfoResponseDto::toDomain) {
            userRemoteDataSource.getUserInfo()
        }
}

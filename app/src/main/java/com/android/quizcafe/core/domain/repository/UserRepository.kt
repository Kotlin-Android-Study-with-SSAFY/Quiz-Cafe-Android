package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.user.request.UpdatePasswordRequest
import com.android.quizcafe.core.domain.model.user.response.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserInfo(): Flow<Resource<UserInfo>>
    fun updateUserNickName(nickName: String): Flow<Resource<Unit>>
    fun deleteUser(): Flow<Resource<Unit>>
    fun getMyQuizBooks(): Flow<Resource<List<QuizBook>>>
    fun updatePassword(request: UpdatePasswordRequest): Flow<Resource<Unit>>
}

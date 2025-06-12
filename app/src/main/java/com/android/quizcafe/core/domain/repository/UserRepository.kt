package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserInfo() : Flow<Resource<UserInfo>>

}

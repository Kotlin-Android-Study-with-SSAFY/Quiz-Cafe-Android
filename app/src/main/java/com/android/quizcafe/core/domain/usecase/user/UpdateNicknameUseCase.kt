package com.android.quizcafe.core.domain.usecase.user

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(nickname: String): Flow<Resource<Unit>> =
        userRepository.updateUserNickName(nickname = nickname)
}

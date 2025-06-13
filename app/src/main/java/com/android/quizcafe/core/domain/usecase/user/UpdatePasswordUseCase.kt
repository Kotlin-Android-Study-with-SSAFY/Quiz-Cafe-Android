package com.android.quizcafe.core.domain.usecase.user

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.user.request.UpdatePasswordRequest
import com.android.quizcafe.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(oldPassword: String, newPassword: String): Flow<Resource<Unit>> =
        userRepository.updatePassword(UpdatePasswordRequest(oldPassword = oldPassword, newPassword = newPassword))
}

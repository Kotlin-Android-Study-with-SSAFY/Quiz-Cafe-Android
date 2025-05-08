package com.android.quizcafe.core.domain.usecase.auth

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.ResetPasswordRequest
import com.android.quizcafe.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: ResetPasswordRequest): Flow<Resource<Unit>> =
        authRepository.resetPassword(request)
}

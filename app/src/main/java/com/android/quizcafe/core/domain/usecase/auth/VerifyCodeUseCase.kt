package com.android.quizcafe.core.domain.usecase.auth

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.VerifyCodeRequest
import com.android.quizcafe.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(request: VerifyCodeRequest): Flow<Resource<Unit>> =
        authRepository.verifyCode(request)
}

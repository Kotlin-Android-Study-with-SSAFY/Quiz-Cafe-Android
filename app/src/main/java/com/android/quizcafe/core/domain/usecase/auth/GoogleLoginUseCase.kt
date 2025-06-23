package com.android.quizcafe.core.domain.usecase.auth

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(idToken: String): Flow<Resource<Unit>> =
        authRepository.googleLogin(idToken)
}

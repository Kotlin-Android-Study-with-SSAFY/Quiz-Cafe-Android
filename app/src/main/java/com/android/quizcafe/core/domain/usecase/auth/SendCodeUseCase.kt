package com.android.quizcafe.core.domain.usecase.auth

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import com.android.quizcafe.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
){
    suspend operator fun invoke(request : SendCodeRequest) : Flow<Resource<Unit>> =
        authRepository.sendCode(request = request)
}
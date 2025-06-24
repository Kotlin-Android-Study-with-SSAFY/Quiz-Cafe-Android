package com.android.quizcafe.core.domain.usecase.quizbook

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookDetailRequest
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.repository.QuizBookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuizBookDetailUseCase @Inject constructor(
    private val quizBookRepository: QuizBookRepository
) {
    operator fun invoke(quizBookDetailRequest: QuizBookDetailRequest): Flow<Resource<QuizBookDetail>> =
        quizBookRepository.getQuizBookDetailById(quizBookDetailRequest)
}

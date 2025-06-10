package com.android.quizcafe.core.domain.usecase.quizbook

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.repository.QuizBookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnmarkQuizBookUseCase @Inject constructor(
    private val quizBookRepository: QuizBookRepository
) {
    operator fun invoke(quizBookId: Long): Flow<Resource<Unit>> =
        quizBookRepository.unmarkQuizBook(quizBookId)
}

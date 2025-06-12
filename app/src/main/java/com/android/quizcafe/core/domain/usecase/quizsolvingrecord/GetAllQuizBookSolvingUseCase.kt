package com.android.quizcafe.core.domain.usecase.quizsolvingrecord

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizBookSolving
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllQuizBookSolvingUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(): Flow<Resource<List<QuizBookSolving>>> =
        quizBookSolvingRepository.getAllQuizBookSolving()
}

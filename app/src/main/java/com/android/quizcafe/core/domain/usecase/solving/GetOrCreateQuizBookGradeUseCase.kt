package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrCreateQuizBookGradeUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(quizBookId: QuizBookId): Flow<Resource<QuizBookGrade>> =
        quizBookSolvingRepository.getOrCreateQuizBookGrade(quizBookId)
}

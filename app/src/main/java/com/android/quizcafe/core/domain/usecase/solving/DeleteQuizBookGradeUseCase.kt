package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteQuizBookGradeUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(quizBookGradeLocalId: QuizBookGradeLocalId): Flow<Resource<Unit>> =
        quizBookSolvingRepository.deleteQuizBookGrade(quizBookGradeLocalId)
}

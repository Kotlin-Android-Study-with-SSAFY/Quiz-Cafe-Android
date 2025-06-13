package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import javax.inject.Inject

class GetQuizBookLocalIdUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(id: QuizBookId) =
        quizBookSolvingRepository.createEmptyQuizBookGrade(id)
}

package com.android.quizcafe.core.domain.usecase.quiz

import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizRepository
import javax.inject.Inject

class GetQuizListByBookIdUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    operator fun invoke(quizBookId: QuizBookId) = quizRepository.getQuizListByBookId(quizBookId)
}

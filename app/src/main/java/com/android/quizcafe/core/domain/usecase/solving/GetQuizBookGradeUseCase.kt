package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import javax.inject.Inject

/**
 * 현재 QuizBookGrade에 있는 모든 QuizGrade 가져오기
 */
class GetQuizBookGradeUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(id: QuizBookGradeLocalId) =
        quizBookSolvingRepository.getQuizBookGrade(id)
}


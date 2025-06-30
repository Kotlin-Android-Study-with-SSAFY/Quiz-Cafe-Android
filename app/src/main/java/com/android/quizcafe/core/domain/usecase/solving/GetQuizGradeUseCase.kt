package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import javax.inject.Inject

/**
 * QuizGrade 가져오기(이전 문제 풀이 기록에 사용)
 */
class GetQuizGradeUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(quizBookGradeLocalId: QuizBookGradeLocalId, quizId: QuizId) =
        quizBookSolvingRepository.getQuizGrade(quizBookGradeLocalId, quizId)
}

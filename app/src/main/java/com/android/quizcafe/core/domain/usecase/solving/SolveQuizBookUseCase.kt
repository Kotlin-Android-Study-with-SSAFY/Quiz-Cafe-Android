package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 퀴즈북 풀이 완료 API 호출
 */
class SolveQuizBookUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(quizBookGradeLocalId: QuizBookGradeLocalId): Flow<Resource<QuizBookGradeServerId>> =
        quizBookSolvingRepository.solveQuizBook(quizBookGradeLocalId)
}

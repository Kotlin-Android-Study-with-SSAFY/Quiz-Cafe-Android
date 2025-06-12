package com.android.quizcafe.core.domain.usecase.solving

import android.util.Log.e
import com.android.quizcafe.core.data.model.solving.request.QuizBookSolvingRequestDto
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.collections.map

/**
 * 퀴즈북 풀이 완료 API 호출
 */
class SolveQuizBookUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(quizBookGradeLocalId: QuizBookGradeLocalId) : Flow<Resource<Unit>> =
        quizBookSolvingRepository.solveQuizBook(quizBookGradeLocalId)

}

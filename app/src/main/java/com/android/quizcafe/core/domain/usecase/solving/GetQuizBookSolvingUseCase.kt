package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuizBookSolvingUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(serverId: QuizBookGradeServerId): Flow<Resource<QuizBookSolving>> =
        quizBookSolvingRepository.getQuizBookSolving(serverId)
}

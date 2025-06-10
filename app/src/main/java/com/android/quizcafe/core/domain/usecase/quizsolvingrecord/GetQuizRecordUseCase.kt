package com.android.quizcafe.core.domain.usecase.quizsolvingrecord

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord
import com.android.quizcafe.core.domain.repository.QuizSolvingRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuizRecordUseCase @Inject constructor(
    private val quizSolvingRecordRepository: QuizSolvingRecordRepository
) {
    operator fun invoke(): Flow<Resource<List<QuizSolvingRecord>>> =
        quizSolvingRecordRepository.getQuizSolvingRecords()
}

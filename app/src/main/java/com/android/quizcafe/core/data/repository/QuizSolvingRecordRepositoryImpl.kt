package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.model.quizsolvingrecord.response.QuizSolvingRecordResponseDto
import com.android.quizcafe.core.data.model.quizsolvingrecord.response.toDomain
import com.android.quizcafe.core.data.remote.datasource.QuizSolvingRecordRemoteDataSource
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord
import com.android.quizcafe.core.domain.repository.QuizSolvingRecordRepository
import com.android.quizcafe.core.network.mapper.apiResponseListToResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuizSolvingRecordRepositoryImpl @Inject constructor(
    private val quizSolvingRecordRemoteDataSource: QuizSolvingRecordRemoteDataSource
) : QuizSolvingRecordRepository {

    override fun getQuizSolvingRecords(): Flow<Resource<List<QuizSolvingRecord>>> =
        apiResponseListToResourceFlow(mapper = QuizSolvingRecordResponseDto::toDomain) {
            quizSolvingRecordRemoteDataSource.getAllQuizSolvingRecordsByUser()
        }
}

package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.quizsolvingrecord.response.QuizSolvingRecordResponseDto
import com.android.quizcafe.core.data.remote.service.QuizSolvingRecordService
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import javax.inject.Inject

class QuizSolvingRecordRemoteDataSource @Inject constructor(
    private val quizSolvingRecordService: QuizSolvingRecordService
) {
    suspend fun getAllQuizSolvingRecordsByUser(): NetworkResult<ApiResponse<List<QuizSolvingRecordResponseDto>>> = quizSolvingRecordService.getQuizSolvingRecords()
}

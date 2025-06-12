package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord
import kotlinx.coroutines.flow.Flow

interface QuizSolvingRecordRepository {
    fun getQuizSolvingRecords(): Flow<Resource<List<QuizSolvingRecord>>>
}

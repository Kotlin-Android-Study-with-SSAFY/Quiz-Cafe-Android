package com.android.quizcafe.core.domain.usecase.quiz

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.feature.main.quiz.QuizHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetQuizHistoryUseCase @Inject constructor() {
    operator fun invoke(): Flow<Resource<List<QuizHistory>>> =
        flowOf(
            Resource.Success(
                listOf(
                    QuizHistory("30분 전", "성준이의 운영체제", 16, 20),
                    QuizHistory("16시간 전", "성민이의 네트워크", 18, 20),
                    QuizHistory("04/01", "재용이의 안드로이드", 19, 20)
                )
            )
        )
}

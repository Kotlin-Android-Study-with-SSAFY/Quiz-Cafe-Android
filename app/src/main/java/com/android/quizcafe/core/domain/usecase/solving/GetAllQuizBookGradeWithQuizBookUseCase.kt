package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.solving.QuizBookGradeWithQuizBook
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllQuizBookGradeWithQuizBookUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(): Flow<Resource<List<QuizBookGradeWithQuizBook>>> =
        quizBookSolvingRepository.getAllQuizBookGradeWithQuizBook()
}

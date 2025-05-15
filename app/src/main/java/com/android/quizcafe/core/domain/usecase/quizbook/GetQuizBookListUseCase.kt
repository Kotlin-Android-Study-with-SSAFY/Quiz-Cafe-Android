package com.android.quizcafe.core.domain.usecase.quizbook

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.repository.QuizBookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuizBookListUseCase @Inject constructor(
    private val quizBookRepository: QuizBookRepository
) {
    operator fun invoke(categoryId: String): Flow<Resource<List<QuizBook>>> =
        quizBookRepository.getQuizBooksByCategory(categoryId)
}

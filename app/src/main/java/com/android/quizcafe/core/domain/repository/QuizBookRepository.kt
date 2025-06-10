package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.request.CategoryRequest
import com.android.quizcafe.core.domain.model.quizbook.response.Category
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookRequest
import kotlinx.coroutines.flow.Flow

interface QuizBookRepository {

    fun getAllCategories(categoryRequest: CategoryRequest): Flow<Resource<List<Category>>>

    fun getQuizBooksByCategory(quizBookRequest: QuizBookRequest): Flow<Resource<List<QuizBook>>>

    fun getQuizBookById(id: QuizBookId): Flow<Resource<QuizBookDetail>>
}

package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.reponse.Category
import com.android.quizcafe.core.domain.model.quizbook.reponse.QuizBook
import com.android.quizcafe.core.domain.model.quizbook.reponse.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.request.CategoryRequest
import kotlinx.coroutines.flow.Flow

interface QuizBookRepository {

    fun getAllCategories(categoryRequest: CategoryRequest): Flow<Resource<List<Category>>>

    fun getQuizBooksByCategory(categoryId: String): Flow<Resource<List<QuizBook>>>

    fun getQuizBookById(id: Long): Flow<Resource<QuizBookDetail>>
}

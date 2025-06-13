package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizbook.request.CategoryRequest
import com.android.quizcafe.core.domain.model.quizbook.response.Category
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookDetailRequest
import com.android.quizcafe.core.domain.model.quizbook.request.QuizBookRequest
import com.android.quizcafe.core.domain.model.value.QuizBookId
import kotlinx.coroutines.flow.Flow

interface QuizBookRepository {

    fun getAllCategories(categoryRequest: CategoryRequest): Flow<Resource<List<Category>>>

    fun getQuizBookById(quizBookId: QuizBookId): Flow<Resource<QuizBook>>

    fun getQuizBookListByCategory(quizBookRequest: QuizBookRequest): Flow<Resource<List<QuizBook>>>

    fun getQuizBookDetailById(quizBookDetailRequest: QuizBookDetailRequest): Flow<Resource<QuizBookDetail>>

    fun markQuizBook(quizBookId: Long): Flow<Resource<Unit>>

    fun unmarkQuizBook(quizBookId: Long): Flow<Resource<Unit>>
}

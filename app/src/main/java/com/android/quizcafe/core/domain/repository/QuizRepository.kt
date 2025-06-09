package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.value.QuizBookId
import kotlinx.coroutines.flow.Flow

interface QuizRepository{

    fun getQuizListByBookId(quizBookId : QuizBookId): Flow<Resource<List<Quiz>>>

}

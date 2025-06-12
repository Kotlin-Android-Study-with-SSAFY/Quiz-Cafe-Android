package com.android.quizcafe.core.domain.usecase.solving

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import com.android.quizcafe.core.domain.usecase.solving.gradingStrategy.GradingStrategyFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 퀴즈 1개에 대해서 채점하기
 * QuizBookGradeLocalId는 null이 되면 안됨
 */
class GradeQuizUseCase @Inject constructor(
    private val quizBookSolvingRepository: QuizBookSolvingRepository
) {
    operator fun invoke(
        quiz : Quiz,
        quizBookGradeLocalId : QuizBookGradeLocalId,
        userAnswer : String
    ) : Flow<Resource<Unit>> = flow {
        val strategy = GradingStrategyFactory.getGradingStrategy(quiz.questionType)
        val isCorrect = strategy.grade(quiz, userAnswer)

        val quizGrade = QuizGrade(
            quizId = quiz.id,
            quizBookGradeLocalId = quizBookGradeLocalId,
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            completedAt = ""
        )

        quizBookSolvingRepository.upsertQuizGrade(quizGrade).collect { result ->
            emit(result)
        }
    }

}

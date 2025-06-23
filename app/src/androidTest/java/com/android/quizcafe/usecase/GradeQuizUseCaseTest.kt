package com.android.quizcafe.usecase

import com.android.quizcafe.core.database.model.grading.QuizBookGradeEntity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.quizcafe.core.database.QuizCafeDatabase
import com.android.quizcafe.core.database.model.quizbook.QuizBookEntity
import com.android.quizcafe.core.database.model.quiz.McqOptionEntity
import com.android.quizcafe.core.database.model.quiz.QuizEntity
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId
import com.android.quizcafe.core.domain.usecase.solving.GradeQuizUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class GradeQuizUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: QuizCafeDatabase

    @Inject
    lateinit var gradeQuizUseCase: GradeQuizUseCase

    private fun generateUniqueId(): Long = System.nanoTime()

    @Before
    fun setup() {
        hiltRule.inject()
        database.clearAllTables()
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun gradeOXQuiz_correctAnswer_withExistingGradeId() = runTest {
        // Given
        val uniqueId = generateUniqueId()
        val quizBookId = QuizBookId(uniqueId)
        val quizId = QuizId(uniqueId + 10)
        val gradeLocalId = QuizBookGradeLocalId(uniqueId + 1000)

        setupTestData(quizBookId, uniqueId)
        setupExistingQuizBookGrade(gradeLocalId, quizBookId)

        val oxQuiz = Quiz(
            id = quizId,
            quizBookId = quizBookId,
            questionType = "OX",
            content = "Kotlin은 JetBrains에서 개발한 언어이다.",
            answer = "O",
            explanation = "맞습니다. Kotlin은 JetBrains에서 개발했습니다.",
        )

        // When
        val results = mutableListOf<Resource<Unit>>()
        gradeQuizUseCase(
            quiz = oxQuiz,
            quizBookGradeLocalId = gradeLocalId,
            userAnswer = "O"
        ).collect(results::add)

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)

        val savedGrade = database.quizBookGradeDao().getQuizBookGrade(gradeLocalId.value)
        assertNotNull("QuizBookGrade should not be null", savedGrade)
        assertTrue("QuizGrade entities should not be empty", savedGrade!!.quizGradeEntities.isNotEmpty())

        val savedQuizGrade = savedGrade.quizGradeEntities.first()
        assertEquals(quizId.value, savedQuizGrade.quizId)
        assertEquals("O", savedQuizGrade.userAnswer)
        assertEquals(true, savedQuizGrade.isCorrect)
        assertEquals(gradeLocalId.value, savedQuizGrade.quizBookGradeLocalId)
    }

    @Test
    fun gradeOXQuiz_wrongAnswer_withExistingGradeId() = runTest {
        // Given
        val uniqueId = generateUniqueId()
        val quizBookId = QuizBookId(uniqueId)
        val quizId = QuizId(uniqueId + 10)
        val gradeId = QuizBookGradeLocalId(uniqueId + 1000)

        setupTestData(quizBookId, uniqueId)
        setupExistingQuizBookGrade(gradeId, quizBookId)

        val oxQuiz = Quiz(
            id = quizId,
            quizBookId = quizBookId,
            questionType = "OX",
            content = "Kotlin은 JetBrains에서 개발한 언어이다.",
            answer = "O",
            explanation = "맞습니다. Kotlin은 JetBrains에서 개발했습니다.",
        )

        // When
        val results = mutableListOf<Resource<Unit>>()
        gradeQuizUseCase.invoke(
            quiz = oxQuiz,
            quizBookGradeLocalId = gradeId,
            userAnswer = "X"
        ).collect(results::add)

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)

        val savedGrade = database.quizBookGradeDao().getQuizBookGrade(gradeId.value)
        assertNotNull("QuizBookGrade should not be null", savedGrade)
        assertTrue("QuizGrade entities should not be empty", savedGrade!!.quizGradeEntities.isNotEmpty())

        val savedQuizGrade = savedGrade.quizGradeEntities.first()
        assertEquals(quizId.value, savedQuizGrade.quizId)
        assertEquals("X", savedQuizGrade.userAnswer)
        assertEquals(false, savedQuizGrade.isCorrect)
    }

    @Test
    fun gradeMCQQuiz_correctAnswer_withExistingGradeId() = runTest {
        // Given
        val uniqueId = generateUniqueId()
        val quizBookId = QuizBookId(uniqueId)
        val quizId = QuizId(uniqueId + 20)
        val gradeId = QuizBookGradeLocalId(uniqueId + 1000)

        setupTestData(quizBookId, uniqueId)
        setupExistingQuizBookGrade(gradeId, quizBookId)

        val mcqQuiz = Quiz(
            id = quizId,
            quizBookId = quizBookId,
            questionType = "MCQ",
            content = "다음 중 Kotlin의 특징이 아닌 것은?",
            answer = "A",
            explanation = "Kotlin은 null 안전성을 제공합니다.",
        )

        val results = mutableListOf<Resource<Unit>>()
        gradeQuizUseCase.invoke(
            quiz = mcqQuiz,
            quizBookGradeLocalId = gradeId,
            userAnswer = "A"
        ).collect(results::add)

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)

        val savedGrade = database.quizBookGradeDao().getQuizBookGrade(gradeId.value)
        assertNotNull("QuizBookGrade should not be null", savedGrade)
        assertTrue("QuizGrade entities should not be empty", savedGrade!!.quizGradeEntities.isNotEmpty())

        val savedQuizGrade = savedGrade.quizGradeEntities.first()
        assertEquals(quizId.value, savedQuizGrade.quizId)
        assertEquals("A", savedQuizGrade.userAnswer)
        assertEquals(true, savedQuizGrade.isCorrect)
        assertEquals(gradeId.value, savedQuizGrade.quizBookGradeLocalId)
    }

    @Test
    fun gradeShortAnswerQuiz_correctAnswer() = runTest {
        // Given
        val uniqueId = generateUniqueId()
        val quizBookId = QuizBookId(uniqueId)
        val quizId = QuizId(uniqueId + 30)
        val gradeId = QuizBookGradeLocalId(uniqueId + 1000)

        setupTestData(quizBookId, uniqueId)
        setupExistingQuizBookGrade(gradeId, quizBookId)

        val shortAnswerQuiz = Quiz(
            id = quizId,
            quizBookId = quizBookId,
            questionType = "SHORT_ANSWER",
            content = "Kotlin에서 불변 변수를 선언하는 키워드는?",
            answer = "val",
            explanation = "val은 불변 변수를 선언하는 키워드입니다.",
        )

        // When
        val results = mutableListOf<Resource<Unit>>()
        gradeQuizUseCase.invoke(
            quiz = shortAnswerQuiz,
            quizBookGradeLocalId = gradeId,
            userAnswer = "val"
        ).collect(results::add)

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)

        val savedGrade = database.quizBookGradeDao().getQuizBookGrade(gradeId.value)
        assertNotNull("QuizBookGrade should not be null", savedGrade)
        assertTrue("QuizGrade entities should not be empty", savedGrade!!.quizGradeEntities.isNotEmpty())

        val savedQuizGrade = savedGrade.quizGradeEntities.first()
        assertEquals(quizId.value, savedQuizGrade.quizId)
        assertEquals("val", savedQuizGrade.userAnswer)
        assertEquals(true, savedQuizGrade.isCorrect)
    }

    @Test
    fun gradeMultipleQuizzes_differentTypes() = runTest {
        // Given
        val uniqueId = generateUniqueId()
        val quizBookId = QuizBookId(uniqueId)
        val gradeId = QuizBookGradeLocalId(uniqueId + 1000)

        setupTestData(quizBookId, uniqueId)
        setupExistingQuizBookGrade(gradeId, quizBookId)

        val quizzes = listOf(
            Quiz(QuizId(uniqueId + 10), quizBookId, "OX", "OX 문제", "O", "설명"),
            Quiz(QuizId(uniqueId + 20), quizBookId, "MCQ", "MCQ 문제", "A", "설명"),
            Quiz(QuizId(uniqueId + 30), quizBookId, "SHORT_ANSWER", "주관식 문제", "val", "설명")
        )

        val userAnswers = listOf("O", "A", "val")

        // When
        quizzes.forEachIndexed { index, quiz ->
            val result = gradeQuizUseCase(
                quiz = quiz,
                quizBookGradeLocalId = gradeId,
                userAnswer = userAnswers[index]
            ).first { it is Resource.Success }
        }

        // Then
        val savedGrade = database.quizBookGradeDao().getQuizBookGrade(gradeId.value)
        assertNotNull("QuizBookGrade should not be null", savedGrade)
        assertEquals(3, savedGrade!!.quizGradeEntities.size)

        savedGrade.quizGradeEntities.forEach { savedQuizGrade ->
            assertEquals(true, savedQuizGrade.isCorrect)
            assertEquals(gradeId.value, savedQuizGrade.quizBookGradeLocalId)
        }
    }

    @Test
    fun gradeQuiz_mixedAnswers() = runTest {
        // Given
        val uniqueId = generateUniqueId()
        val quizBookId = QuizBookId(uniqueId)
        val gradeId = QuizBookGradeLocalId(uniqueId + 1000)

        setupTestData(quizBookId, uniqueId)
        setupExistingQuizBookGrade(gradeId, quizBookId)

        // When
        val testCases = listOf(
            Triple(Quiz(QuizId(uniqueId + 10), quizBookId, "OX", "OX 문제", "O", "설명"), "X", false),
            Triple(Quiz(QuizId(uniqueId + 20), quizBookId, "MCQ", "MCQ 문제", "A", "설명"), "A", true),
            Triple(Quiz(QuizId(uniqueId + 30), quizBookId, "SHORT_ANSWER", "주관식 문제", "val", "설명"), "var", false)
        )

        testCases.forEach { (quiz, userAnswer, expectedCorrect) ->
            gradeQuizUseCase.invoke(
                quiz = quiz,
                quizBookGradeLocalId = gradeId,
                userAnswer = userAnswer
            ).first { it is Resource.Success }
        }

        // Then
        val savedGrade = database.quizBookGradeDao().getQuizBookGrade(gradeId.value)
        assertNotNull("QuizBookGrade should not be null", savedGrade)
        assertEquals(3, savedGrade!!.quizGradeEntities.size)

        val correctCount = savedGrade.quizGradeEntities.count { it.isCorrect == true }
        assertEquals(1, correctCount)
    }

    @Test
    fun gradeQuiz_nonExistentGradeId_shouldFail() = runTest {
        // Given
        val uniqueId = generateUniqueId()
        val quizBookId = QuizBookId(uniqueId)
        val quizId = QuizId(uniqueId + 10)
        val nonExistentGradeId = QuizBookGradeLocalId(uniqueId + 9999)

        setupTestData(quizBookId, uniqueId)

        val oxQuiz = Quiz(
            id = quizId,
            quizBookId = quizBookId,
            questionType = "OX",
            content = "테스트 문제",
            answer = "O",
            explanation = "설명",
        )

        // When
        val results = mutableListOf<Resource<Unit>>()
        gradeQuizUseCase.invoke(
            quiz = oxQuiz,
            quizBookGradeLocalId = nonExistentGradeId,
            userAnswer = "O"
        ).collect(results::add)

        // Then
        assertTrue(results.any { it is Resource.Loading })
        assertTrue(results.any { it is Resource.Failure })
    }

    private suspend fun setupTestData(quizBookId: QuizBookId, baseId: Long) {
        val quizBookEntity = QuizBookEntity(
            id = quizBookId.value,
            version = 1L,
            category = "TEST_$baseId",
            title = "테스트 퀴즈북 $baseId",
            description = "테스트 퀴즈북입니다. ID: $baseId",
            level = "BEGINNER",
            createdBy = "testuser_$baseId",
            createdAt = "2023-01-01T00:00:00Z",
            totalQuizzes = 3
        )
        database.quizBookDao().upsertQuizBook(quizBookEntity)

        val quizEntities = mutableListOf<QuizEntity>()
        val mcqOptions = mutableListOf<McqOptionEntity>()

        // OX 문제
        val quiz1 = QuizEntity(
            id = baseId + 10,
            quizBookId = quizBookId.value,
            questionType = "OX",
            content = "Kotlin은 JetBrains에서 개발한 언어이다.",
            answer = "O",
            explanation = "맞습니다. Kotlin은 JetBrains에서 개발했습니다.",
        )
        quizEntities.add(quiz1)

        // MCQ 문제
        val quiz2 = QuizEntity(
            id = baseId + 20,
            quizBookId = quizBookId.value,
            questionType = "MCQ",
            content = "다음 중 Kotlin의 특징이 아닌 것은?",
            answer = "A",
            explanation = "Kotlin은 null 안전성을 제공합니다.",
        )
        quizEntities.add(quiz2)

        val mcqOptionsForQuiz2 = listOf(
            McqOptionEntity(baseId + 100, baseId + 20, 1, "메모리 관리가 어렵다", true),
            McqOptionEntity(baseId + 101, baseId + 20, 2, "Null 안전성을 제공한다", false),
            McqOptionEntity(baseId + 102, baseId + 20, 3, "Java와 상호 운용 가능하다", false),
            McqOptionEntity(baseId + 103, baseId + 20, 4, "함수형 프로그래밍을 지원한다", false),
            McqOptionEntity(baseId + 104, baseId + 20, 5, "간결한 문법을 제공한다", false)
        )
        mcqOptions.addAll(mcqOptionsForQuiz2)

        // SHORT_ANSWER 문제
        val quiz3 = QuizEntity(
            id = baseId + 30,
            quizBookId = quizBookId.value,
            questionType = "SHORT_ANSWER",
            content = "Kotlin에서 변수를 선언할 때 사용하는 키워드 중 불변 변수를 선언하는 키워드는?",
            answer = "val",
            explanation = "val은 불변 변수(읽기 전용)를 선언하는 키워드입니다.",
        )
        quizEntities.add(quiz3)

        database.quizDao().upsertQuizList(quizEntities)
        database.quizDao().upsertMcqOptions(mcqOptions)
    }

    private suspend fun setupExistingQuizBookGrade(
        gradeId: QuizBookGradeLocalId,
        quizBookId: QuizBookId
    ) {
        val quizBookGradeEntity = QuizBookGradeEntity(
            localId = gradeId.value,
            serverId = null,
            quizBookId = quizBookId.value,
        )
        database.quizBookGradeDao().upsertQuizBookGrade(quizBookGradeEntity)
    }
}

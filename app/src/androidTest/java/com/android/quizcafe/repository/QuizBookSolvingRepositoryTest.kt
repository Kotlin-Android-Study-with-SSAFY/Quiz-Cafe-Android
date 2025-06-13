package com.android.quizcafe.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.quizcafe.core.data.repository.QuizBookSolvingRepositoryImpl
import com.android.quizcafe.core.database.QuizCafeDatabase
import com.android.quizcafe.core.database.model.quizbook.QuizBookEntity
import com.android.quizcafe.core.database.model.quiz.McqOptionEntity
import com.android.quizcafe.core.database.model.quiz.QuizEntity
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class QuizBookSolvingRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: QuizCafeDatabase

    @Inject
    lateinit var repository: QuizBookSolvingRepositoryImpl

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)

        // 200 OK 응답 설정
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                    {
                        "status": "success",
                        "code": 200,
                        "message": "퀴즈북 풀이 완료",
                        "data": 1
                    }
                    """.trimIndent()
                )
                .addHeader("Content-Type", "application/json")
        )

        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
        mockWebServer.shutdown()
    }

    @Test
    fun createEmptyQuizBookGrade_success() = runTest {
        database.clearAllTables()
        val quizBookId = QuizBookId(1L)
        setupTestData(quizBookId)

        val results = mutableListOf<Resource<*>>()
        repository.createEmptyQuizBookGrade(quizBookId).collect(results::add)

        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)
    }

    @Test
    fun upsertQuizGrade_success() = runTest {
        database.clearAllTables()
        val quizBookId = QuizBookId(1L)
        setupTestData(quizBookId)

        // 먼저 퀴즈북 풀이 생성
        val quizBookGradeLocalId = repository.createEmptyQuizBookGrade(quizBookId).first { it is Resource.Success }
            .let { (it as Resource.Success).data }

        val quizGrade = QuizGrade(
            localId = 1L,
            quizId = QuizId(1L),
            quizBookGradeLocalId = quizBookGradeLocalId,
            userAnswer = "O",
            isCorrect = true,
            completedAt = "2023-01-01T00:00:00Z"
        )

        // When
        val results = mutableListOf<Resource<Unit>>()
        repository.upsertQuizGrade(quizGrade).collect(results::add)

        // Then - 기본 검증
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)

        // Then - 로컬 데이터베이스에서 실제 저장된 데이터 조회 및 비교
        val savedQuizBookGrade = database.quizBookGradeDao().getQuizBookGrade(quizBookGradeLocalId.value)
        if (savedQuizBookGrade == null) {
            fail("Saved QuizBookGrade should not be null")
        }
        val savedQuizGrades = savedQuizBookGrade!!.quizGradeEntities

        // 저장된 퀴즈 답안이 1개인지 확인
        assertEquals(1, savedQuizGrades.size)

        val savedQuizGrade = savedQuizGrades.first()

        // 원본 데이터와 저장된 데이터 비교
        assertEquals(quizGrade.quizId.value, savedQuizGrade.quizId)
        assertEquals(quizGrade.quizBookGradeLocalId.value, savedQuizGrade.quizBookGradeLocalId)
        assertEquals(quizGrade.userAnswer, savedQuizGrade.userAnswer)
        assertEquals(quizGrade.isCorrect, savedQuizGrade.isCorrect)
        assertEquals(quizGrade.completedAt, savedQuizGrade.completedAt)
        assertEquals(quizGrade.memo, savedQuizGrade.memo)

        // Repository를 통해 조회한 데이터와도 비교
        val repositoryResult = repository.getQuizBookGrade(quizBookGradeLocalId)
            .first { it is Resource.Success }
            .let { (it as Resource.Success).data }

        assertEquals(1, repositoryResult.quizGrades.size)
        val repositoryQuizGrade = repositoryResult.quizGrades.first()

        // Repository 결과와 원본 데이터 비교
        assertEquals(quizGrade.quizId, repositoryQuizGrade.quizId)
        assertEquals(quizGrade.quizBookGradeLocalId, repositoryQuizGrade.quizBookGradeLocalId)
        assertEquals(quizGrade.userAnswer, repositoryQuizGrade.userAnswer)
        assertEquals(quizGrade.isCorrect, repositoryQuizGrade.isCorrect)
        assertEquals(quizGrade.completedAt, repositoryQuizGrade.completedAt)
    }

    @Test
    fun solveQuizBook_success_oneWrongAnswer() = runTest {
        // Given
        database.clearAllTables()
        val quizBookId = QuizBookId(1L)
        setupTestData(quizBookId)

        val quizBookGradeLocalId = repository.createEmptyQuizBookGrade(quizBookId)
            .first { it is Resource.Success }
            .let { (it as Resource.Success).data }

        // 3개 문제 중 1번만 틀리고 나머지는 맞음
        val quizGrades = listOf(
            // 1번 문제: OX 문제 - 틀림
            QuizGrade(
                localId = 1L,
                quizId = QuizId(1L),
                quizBookGradeLocalId = quizBookGradeLocalId,
                userAnswer = "X",
                isCorrect = false,
                completedAt = "2023-01-01T00:00:00Z"
            ),
            // 2번 문제: MCQ 문제 - 맞음
            QuizGrade(
                localId = 2L,
                quizId = QuizId(2L),
                quizBookGradeLocalId = quizBookGradeLocalId,
                userAnswer = "A",
                isCorrect = true,
                completedAt = "2023-01-01T00:00:01Z"
            ),
            // 3번 문제: SHORT_ANSWER 문제 - 맞음
            QuizGrade(
                localId = 3L,
                quizId = QuizId(3L),
                quizBookGradeLocalId = quizBookGradeLocalId,
                userAnswer = "val",
                isCorrect = true,
                completedAt = "2023-01-01T00:00:02Z"
            )
        )

        // 모든 퀴즈 답안 저장
        quizGrades.forEach { quizGrade ->
            repository.upsertQuizGrade(quizGrade).first { it is Resource.Success }
        }

        // When
        val solveResults = mutableListOf<Resource<QuizBookGradeServerId>>()
        repository.solveQuizBook(quizBookGradeLocalId).collect(solveResults::add)

        // Then
        assertEquals(2, solveResults.size)
        assertTrue(solveResults[0] is Resource.Loading)
        assertTrue(solveResults[1] is Resource.Success)

        val request = mockWebServer.takeRequest()
        val requestBody = request.body.readUtf8()

        // request JSON 파싱
        val requestData = Json.parseToJsonElement(requestBody).jsonObject

        // 기본 필드 검증
        assertTrue(requestData.containsKey("quizBookId"))
        assertTrue(requestData.containsKey("version"))
        assertTrue(requestData.containsKey("totalQuizzes"))
        assertTrue(requestData.containsKey("correctCount"))
        assertTrue(requestData.containsKey("quizzes"))

        // 값 검증 - 1개 틀림, 2개 맞음
        assertEquals(1L, requestData["quizBookId"]?.jsonPrimitive?.long)
        assertEquals(1L, requestData["version"]?.jsonPrimitive?.long)
        assertEquals(3, requestData["totalQuizzes"]?.jsonPrimitive?.int) // 총 3문제
        assertEquals(2, requestData["correctCount"]?.jsonPrimitive?.int) // 2개 맞음 (1개 틀림)

        val quizzes = requestData["quizzes"]?.jsonArray
        assertEquals(3, quizzes?.size) // setupTestData에서 3개 문제 생성

        // 각 문제별 상세 검증
        val quiz1 = quizzes?.get(0)?.jsonObject
        assertEquals(1L, quiz1?.get("quizId")?.jsonPrimitive?.long)
        assertEquals("X", quiz1?.get("userAnswer")?.jsonPrimitive?.content)
        assertEquals(false, quiz1?.get("isCorrect")?.jsonPrimitive?.boolean)

        val quiz2 = quizzes?.get(1)?.jsonObject
        assertEquals(2L, quiz2?.get("quizId")?.jsonPrimitive?.long)
        assertEquals("A", quiz2?.get("userAnswer")?.jsonPrimitive?.content)
        assertEquals(true, quiz2?.get("isCorrect")?.jsonPrimitive?.boolean)

        val quiz3 = quizzes?.get(2)?.jsonObject
        assertEquals(3L, quiz3?.get("quizId")?.jsonPrimitive?.long)
        assertEquals("val", quiz3?.get("userAnswer")?.jsonPrimitive?.content)
        assertEquals(true, quiz3?.get("isCorrect")?.jsonPrimitive?.boolean)
    }

    private suspend fun setupTestData(quizBookId: QuizBookId) {
        // QuizBook 데이터 삽입 - 새로운 필드 포함
        val quizBookEntity = QuizBookEntity(
            id = quizBookId.value,
            version = 1L,
            category = "TEST",
            title = "테스트 퀴즈북",
            description = "테스트 퀴즈북입니다.",
            level = "BEGINNER",
            createdBy = "testuser",
            createdAt = "2023-01-01T00:00:00Z",
            totalQuizzes = 3
        )
        database.quizBookDao().upsertQuizBook(quizBookEntity)

        // Quiz 데이터 삽입 (3개 문제 - 각기 다른 유형)
        val quizEntities = mutableListOf<QuizEntity>()
        val mcqOptions = mutableListOf<McqOptionEntity>()

        // 1번 문제: OX 문제
        val quiz1 = QuizEntity(
            id = 1L,
            quizBookId = quizBookId.value,
            questionType = "OX",
            content = "Kotlin은 JetBrains에서 개발한 언어이다.",
            answer = "O",
            explanation = "맞습니다. Kotlin은 JetBrains에서 개발했습니다.",
        )
        quizEntities.add(quiz1)

        // 2번 문제: MCQ 문제
        val quiz2 = QuizEntity(
            id = 2L,
            quizBookId = quizBookId.value,
            questionType = "MCQ",
            content = "다음 중 Kotlin의 특징이 아닌 것은?",
            answer = "A",
            explanation = "Kotlin은 null 안전성을 제공합니다.",
        )
        quizEntities.add(quiz2)

        // 2번 문제의 MCQ 선택지 5개
        val mcqOptionsForQuiz2 = listOf(
            McqOptionEntity(1L, 2L, 1, "메모리 관리가 어렵다", true),
            McqOptionEntity(2L, 2L, 2, "Null 안전성을 제공한다", false),
            McqOptionEntity(3L, 2L, 3, "Java와 상호 운용 가능하다", false),
            McqOptionEntity(4L, 2L, 4, "함수형 프로그래밍을 지원한다", false),
            McqOptionEntity(5L, 2L, 5, "간결한 문법을 제공한다", false)
        )
        mcqOptions.addAll(mcqOptionsForQuiz2)

        // 3번 문제: SHORT_ANSWER 문제
        val quiz3 = QuizEntity(
            id = 3L,
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
}

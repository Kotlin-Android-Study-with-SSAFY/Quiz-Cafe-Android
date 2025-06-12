package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.data.mapper.solving.toDomain
import com.android.quizcafe.core.data.mapper.quiz.toEntity
import com.android.quizcafe.core.data.mapper.quizbook.toDomain
import com.android.quizcafe.core.data.model.solving.request.McqOptionSolvingRequestDto
import com.android.quizcafe.core.data.model.solving.request.QuizBookSolvingRequestDto
import com.android.quizcafe.core.data.model.solving.request.QuizSolvingRequestDto
import com.android.quizcafe.core.data.model.solving.response.QuizBookSolvingResponseDto
import com.android.quizcafe.core.data.remote.datasource.QuizBookSolvingRemoteDataSource
import com.android.quizcafe.core.data.util.LocalErrorCode
import com.android.quizcafe.core.database.dao.quiz.QuizDao
import com.android.quizcafe.core.database.dao.quiz.QuizGradeDao
import com.android.quizcafe.core.database.dao.quizBook.QuizBookDao
import com.android.quizcafe.core.database.dao.quizBook.QuizBookGradeDao
import com.android.quizcafe.core.database.model.QuizBookEntity
import com.android.quizcafe.core.database.model.grading.QuizBookGradeEntity
import com.android.quizcafe.core.database.model.grading.QuizGradeEntity
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import com.android.quizcafe.core.network.mapper.apiResponseListToResourceFlow
import com.android.quizcafe.core.network.mapper.apiResponseToResourceFlow
import com.android.quizcafe.core.network.model.onErrorOrException
import com.android.quizcafe.core.network.model.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.collections.map
class QuizBookSolvingRepositoryImpl @Inject constructor(
    private val quizGradeDao: QuizGradeDao,
    private val quizDao: QuizDao,
    private val quizBookDao: QuizBookDao,
    private val quizBookGradeDao: QuizBookGradeDao,
    private val remoteDataSource: QuizBookSolvingRemoteDataSource
) : QuizBookSolvingRepository {

    /**
     * 퀴즈북 풀기 시작할 때 호출
     * localId값 반환
     */
    override fun createEmptyQuizBookGrade(quizBookId: QuizBookId): Flow<Resource<QuizBookGradeLocalId>> = flow {
        emit(Resource.Loading)
        val entity = QuizBookGradeEntity(quizBookId = quizBookId.value)
        val generatedId = quizBookGradeDao.upsertQuizBookGrade(entity)

        if (generatedId <= 0L) {
            emit(Resource.Failure(errorMessage = "QuizBookGrade 생성 실패", code = LocalErrorCode.ROOM_ERROR))
        } else {
            emit(Resource.Success(QuizBookGradeLocalId(generatedId)))
        }
    }.catch { e ->
        emit(Resource.Failure(errorMessage = "QuizBookGrade 생성 중 오류: ${e.message}", code = LocalErrorCode.ROOM_ERROR))
    }

    // 퀴즈북 풀이 기록 가져오기
    override fun getQuizBookGrade(id: QuizBookGradeLocalId): Flow<Resource<QuizBookGrade>> = flow {
        emit(Resource.Loading)
        val quizBookGradeRelation = quizBookGradeDao.getQuizBookGrade(id.value)

        if (quizBookGradeRelation == null) {
            emit(Resource.Failure(errorMessage = "퀴즈북 풀이 기록을 찾을 수 없습니다", code = LocalErrorCode.ROOM_ERROR))
        } else {
            val quizBookGrade = quizBookGradeRelation.toDomain()
            emit(Resource.Success(quizBookGrade))
        }
    }.catch { e ->
        emit(Resource.Failure(errorMessage = "퀴즈북 풀이 기록 조회 중 오류: ${e.message}", code = LocalErrorCode.ROOM_ERROR))
    }

    override fun getQuizBookSolving(id: QuizBookGradeServerId): Flow<Resource<QuizBookSolving>> = flow {
        val quizBookSolvingId = id.value
        if (quizBookSolvingId == null) {
            emit(Resource.Failure("quizBookGradeServerId가 null입니다", HttpStatus.UNKNOWN))
            return@flow
        } else {
            apiResponseToResourceFlow(mapper = QuizBookSolvingResponseDto::toDomain) {
                remoteDataSource.getQuizBookSolving(quizBookSolvingId)
            }
        }
    }

    override fun getAllQuizBookSolving(): Flow<Resource<List<QuizBookSolving>>> = flow {
        apiResponseListToResourceFlow(mapper = QuizBookSolvingResponseDto::toDomain) {
            remoteDataSource.getAllQuizBookSolvingByUser()
        }
    }

    // 퀴즈 1개 풀이 기록 저장 및 수정
    override fun upsertQuizGrade(quizGrade: QuizGrade): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        val result = quizGradeDao.upsert(quizGrade.toEntity())

        // -1은 업데이트 성공, 양수는 삽입 성공
        if (result == -1L || result > 0L) {
            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Failure(errorMessage = "퀴즈 풀이 기록 저장 실패 result : $result", code = LocalErrorCode.ROOM_ERROR))
        }
    }.catch { e ->
        emit(Resource.Failure(errorMessage = "퀴즈 풀이 기록 저장 중 오류: ${e.message}", code = LocalErrorCode.ROOM_ERROR))
    }

    // 로컬에서 퀴즈북 풀이 기록 가져와 requestDto로 변환 후 퀴즈북 풀이 완료 API 요청하기
    override fun solveQuizBook(localId: QuizBookGradeLocalId): Flow<Resource<QuizBookGradeServerId>> = flow {
        emit(Resource.Loading)
        val (quizBookGradeEntity, quizGradeEntities) = getQuizBookGradeData(localId)
        val quizBookEntity = getQuizBookEntity(quizBookGradeEntity.quizBookId)

        val requestDto = createQuizBookSolvingRequest(
            quizBookGradeEntity = quizBookGradeEntity,
            quizBookEntity = quizBookEntity,
            quizGradeEntities = quizGradeEntities
        )

        remoteDataSource.solveQuizBook(requestDto)
            .onSuccess { response ->
                response.data?.let { serverId ->
                    quizBookGradeDao.deleteQuizBookGrade(localId.value)
                    // TODO : 삭제 실패 했을 때 처리
                    emit(Resource.Success(QuizBookGradeServerId(serverId)))
                } ?: emit(Resource.Failure("서버 응답 데이터가 null입니다", HttpStatus.UNKNOWN))
            }
            .onErrorOrException { code, message ->
                emit(Resource.Failure(message ?: "퀴즈북 풀이 제출 실패", code))
            }
    }.catch { e ->
        emit(Resource.Failure("퀴즈북 제출 중 오류 발생: ${e.message}", LocalErrorCode.ROOM_ERROR))
    }

    // 로컬에서 퀴즈북 풀이 기록 가져오기 - null 체크 추가
    private suspend fun getQuizBookGradeData(
        localId: QuizBookGradeLocalId
    ): Pair<QuizBookGradeEntity, List<QuizGradeEntity>> {
        val quizBookGradeRelation = quizBookGradeDao.getQuizBookGrade(localId.value)
            ?: throw IllegalStateException("QuizBookGrade not found for localId: ${localId.value}")

        return Pair(
            quizBookGradeRelation.quizBookGradeEntity,
            quizBookGradeRelation.quizGradeEntities
        )
    }

    // 로컬에서 퀴즈북 정보 가져오기 - null 체크 추가
    private suspend fun getQuizBookEntity(quizBookId: Long): QuizBookEntity {
        return quizBookDao.getQuizBookById(quizBookId)
            ?: throw IllegalStateException("QuizBook not found for id: $quizBookId")
    }

    // 퀴즈북 풀이 요청 DTO 생성
    private suspend fun createQuizBookSolvingRequest(
        quizBookGradeEntity: QuizBookGradeEntity,
        quizBookEntity: QuizBookEntity,
        quizGradeEntities: List<QuizGradeEntity>
    ): QuizBookSolvingRequestDto {
        val correctCount = quizGradeEntities.count { it.isCorrect == true }
        val quizzes = quizGradeEntities.mapNotNull { quizGrade ->
            createQuizSolvingRequest(quizGrade)
        }

        return QuizBookSolvingRequestDto(
            quizBookId = quizBookGradeEntity.quizBookId,
            version = quizBookEntity.version,
            totalQuizzes = quizGradeEntities.size,
            correctCount = correctCount,
            quizzes = quizzes
        )
    }

    // 퀴즈 1개 풀이 요청 DTO 생성 - null 체크 추가
    private suspend fun createQuizSolvingRequest(
        quizGrade: QuizGradeEntity
    ): QuizSolvingRequestDto? {
        val quizWithOptions = quizDao.getQuizWithOptionsById(quizGrade.quizId)

        if (quizWithOptions == null) {
            // 로그 추가 고려
            return null
        }

        return QuizSolvingRequestDto(
            quizId = quizGrade.quizId,
            questionType = quizWithOptions.quizEntity.questionType,
            content = quizWithOptions.quizEntity.content,
            answer = quizWithOptions.quizEntity.answer,
            explanation = quizWithOptions.quizEntity.explanation,
            memo = quizGrade.memo,
            userAnswer = quizGrade.userAnswer,
            isCorrect = quizGrade.isCorrect,
            mcqOptions = quizWithOptions.mcqOptions.map { mcqOption ->
                McqOptionSolvingRequestDto(
                    optionNumber = mcqOption.optionNumber,
                    optionContent = mcqOption.optionContent,
                    isCorrect = mcqOption.isCorrect
                )
            }
        )
    }
}

package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.mapper.toDomain
import com.android.quizcafe.core.data.mapper.toEntity
import com.android.quizcafe.core.data.model.solving.request.McqOptionSolvingRequestDto
import com.android.quizcafe.core.data.model.solving.request.QuizBookSolvingRequestDto
import com.android.quizcafe.core.data.model.solving.request.QuizSolvingRequestDto
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
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import com.android.quizcafe.core.network.model.onErrorOrException
import com.android.quizcafe.core.network.model.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
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
    override fun createEmptyQuizBookGrade(quizBookId : QuizBookId) : Flow<Resource<QuizBookGradeLocalId>> = flow{
        emit(Resource.Loading)
        val entity = QuizBookGradeEntity(quizBookId = quizBookId)
        val gradeId= quizBookGradeDao.insert(entity)
        if(gradeId == QuizBookGradeLocalId(-1L)){
            emit(Resource.Failure(errorMessage = "insert 실패", code = LocalErrorCode.ROOM_ERROR))
        }else{
            emit(Resource.Success(gradeId))
        }
    }

    // 퀴즈북 풀이 기록 가져오기
    override fun getQuizBookGrade(id : QuizBookGradeLocalId) : Flow<Resource<QuizBookGrade>> = flow{
        emit(Resource.Loading)
        val grade = quizBookGradeDao.getQuizBookGrade(id).toDomain()
        emit(Resource.Success(grade))
    }

    // 퀴즈 1개 풀이 기록 저장 및 수정
    override fun upsertQuizGrade(quizGrade: QuizGrade): Flow<Resource<Unit>> = flow{
        emit(Resource.Loading)
        val result = quizGradeDao.upsert(quizGrade.toEntity())
        if(result == -1L){
            emit(Resource.Failure(errorMessage = "upsert 실패", code = LocalErrorCode.ROOM_ERROR))
        }else{
            emit(Resource.Success(Unit))
        }
    }

    // 퀴즈북 풀이 완료 API 요청
    // 로컬에서 퀴즈북 풀이 기록 끌고와서 Dto로 변환 후 API 요청하기
    override fun solveQuizBook(localId: QuizBookGradeLocalId): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)

        try {
            val (quizBookGradeEntity, quizGradeEntities) = getQuizBookGradeData(localId)
            val quizBookEntity = getQuizBookEntity(quizBookGradeEntity.quizBookId)

            val requestDto = createQuizBookSolvingRequest(
                quizBookGradeEntity = quizBookGradeEntity,
                quizBookEntity = quizBookEntity,
                quizGradeEntities = quizGradeEntities
            )

            remoteDataSource.solveQuizBook(requestDto)
                .onSuccess { response ->
                    response.data?.let {
                        emit(Resource.Success(Unit))
                    } ?: emit(Resource.Failure.NullData)
                }
                .onErrorOrException { code, message ->
                    emit(Resource.Failure(message ?: "퀴즈북 풀이 제출 실패", code))
                }

        } catch (e: Exception) {
            emit(Resource.Failure("퀴즈북 제출 중 오류 발생: ${e.message}", LocalErrorCode.ROOM_ERROR))
        }
    }

    // 로컬에서 퀴즈북 풀이 기록 가져오기
    private suspend fun FlowCollector<Resource<Unit>>.getQuizBookGradeData(
        localId: QuizBookGradeLocalId
    ): Pair<QuizBookGradeEntity, List<QuizGradeEntity>> {
        val quizBookGradeRelation = quizBookGradeDao.getQuizBookGrade(localId)

        if (quizBookGradeRelation == null) throw IllegalStateException("QuizBookGrade not found")

        return Pair(
            quizBookGradeRelation.quizBookGradeEntity,
            quizBookGradeRelation.quizGradeEntities
        )
    }

    // 로컬에서 퀴즈북 정보 가져오기
    private suspend fun FlowCollector<Resource<Unit>>.getQuizBookEntity(
        quizBookId: QuizBookId
    ): QuizBookEntity {
        val quizBookEntity = quizBookDao.getQuizBookById(quizBookId.value)

        if (quizBookEntity == null) {
            emit(Resource.Failure("퀴즈북 정보를 찾을 수 없습니다", LocalErrorCode.ROOM_ERROR))
            throw IllegalStateException("QuizBook not found")
        }

        return quizBookEntity
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
            quizBookId = quizBookGradeEntity.quizBookId.value,
            version = quizBookEntity.version,
            totalQuizzes = quizGradeEntities.size,
            correctCount = correctCount,
            quizzes = quizzes
        )
    }

    // 퀴즈 1개 풀이 요청 DTO 생성
    private suspend fun createQuizSolvingRequest(
        quizGrade: QuizGradeEntity
    ): QuizSolvingRequestDto? {
        val quizWithOptions = quizDao.getQuizWithOptionsById(quizGrade.quizId)
        val quizEntity = quizWithOptions?.quizEntity ?: return null

        return QuizSolvingRequestDto(
            quizId = quizGrade.quizId,
            questionType = quizEntity.questionType,
            content = quizEntity.content,
            answer = quizEntity.answer,
            explanation = quizEntity.explanation,
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

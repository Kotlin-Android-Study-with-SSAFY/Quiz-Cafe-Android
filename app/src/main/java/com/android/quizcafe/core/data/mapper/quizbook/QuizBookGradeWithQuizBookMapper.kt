package com.android.quizcafe.core.data.mapper.quizbook

import com.android.quizcafe.core.database.model.grading.QuizBookGradeWithQuizBookData
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.solving.QuizBookGradeWithQuizBook
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId

fun QuizBookGradeWithQuizBookData.toDomain() = QuizBookGradeWithQuizBook(
    quizBook = QuizBook(
        id = quizBook.id,
        version = quizBook.version,
        category = quizBook.category,
        title = quizBook.title,
        description = quizBook.description,
        level = quizBook.level,
        ownerName = quizBook.createdBy,
        totalQuizzes = quizBook.totalQuizzes,
        totalComments = 0,
        totalSaves = 0,
        createdAt = quizBook.createdAt,
        quizList = emptyList()
    ),
    quizBookGrade = QuizBookGrade(
        localId = QuizBookGradeLocalId(grade.localId),
        serverId = QuizBookGradeServerId(grade.serverId),
        quizBookId = QuizBookId(grade.quizBookId),
        elapsedTime = grade.elapsedTime
    )
)

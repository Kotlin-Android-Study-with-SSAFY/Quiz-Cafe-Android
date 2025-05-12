package com.android.quizcafe.feature.quiz.solve.component

sealed class AnswerState {
    object Normal : AnswerState() // 채점 전 선택 x
    object Selected : AnswerState() // 채점 전 선택
    object Correct : AnswerState() // 채점 후 정답
    object Incorrect : AnswerState() //채점 후 오답
}

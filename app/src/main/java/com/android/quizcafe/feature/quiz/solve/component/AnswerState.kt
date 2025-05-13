package com.android.quizcafe.feature.quiz.solve.component

enum class AnswerState {
    DEFAULT,// 채점 전 선택 x
    SELECTED, // 채점 전 선택
    CORRECT, // 채점 후 정답
    INCORRECT//채점 후 오답
}

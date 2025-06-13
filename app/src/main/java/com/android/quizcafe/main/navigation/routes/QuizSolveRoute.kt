package com.android.quizcafe.main.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
object QuizSolve

@Serializable
data class Solve(val quizBookId: Long)

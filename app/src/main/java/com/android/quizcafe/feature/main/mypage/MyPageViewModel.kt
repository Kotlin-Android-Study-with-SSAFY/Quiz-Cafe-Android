package com.android.quizcafe.feature.main.mypage

import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    // 실제로는 UserInfoUseCase 등 DI 주입
) : BaseViewModel<MyPageViewState, MyPageIntent, MyPageEffect>(
    initialState = MyPageViewState()
) {
    override suspend fun handleIntent(intent: MyPageIntent) {
        when (intent) {
            is MyPageIntent.LoadUserInfo -> loadUserInfo()
            is MyPageIntent.ClickStats -> emitEffect(MyPageEffect.NavigateToStats)
            is MyPageIntent.ClickAlarm -> emitEffect(MyPageEffect.NavigateToAlarm)
            is MyPageIntent.ClickChangePw -> emitEffect(MyPageEffect.NavigateToChangePw)
            is MyPageIntent.ClickMyQuizSet -> emitEffect(MyPageEffect.NavigateToMyQuizSet)
            is MyPageIntent.FailLoadUserInfo -> emitEffect(MyPageEffect.ShowError(intent.errorMessage))
            else -> Unit
        }
    }

    override fun reduce(currentState: MyPageViewState, intent: MyPageIntent): MyPageViewState {
        return when (intent) {
            is MyPageIntent.LoadUserInfo -> currentState.copy(isLoading = true, errorMessage = null)
            is MyPageIntent.SuccessLoadUserInfo -> currentState.copy(
                userName = intent.userName,
                solvedCount = intent.solvedCount,
                myQuizSetCount = intent.myQuizSetCount,
                quizSolvingRecord = intent.quizSolvingRecord,
                joinDateStr = intent.joinDateStr,
                isLoading = false,
                errorMessage = null
            )
            is MyPageIntent.FailLoadUserInfo -> currentState.copy(isLoading = false, errorMessage = intent.errorMessage)
            else -> currentState
        }
    }

    // 임시 Mock Data, 실제에선 UseCase 호출로 대체
    private suspend fun loadUserInfo() {
        try {
            // 예시용: 1초 뒤 샘플 데이터 반환
            kotlinx.coroutines.delay(500)
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val today = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
            val start = addDaysToCalendar(today, -364)
            val quizHistory = mutableMapOf<String, Int>()
            for (i in 0..364) {
                val cal = addDaysToCalendar(start, i)
                quizHistory[sdf.format(cal.time)] = (0..4).random()
            }
            sendIntent(
                MyPageIntent.SuccessLoadUserInfo(
                    userName = "빵빠야",
                    solvedCount = 1205,
                    myQuizSetCount = 5,
                    quizSolvingRecord = quizHistory,
                    joinDateStr = sdf.format(start.time)
                )
            )
        } catch (e: Exception) {
            sendIntent(MyPageIntent.FailLoadUserInfo(e.message ?: "사용자 정보를 불러오지 못했습니다."))
        }
    }
}

fun addDaysToCalendar(calendar: Calendar, days: Int): Calendar {
    val cal = calendar.clone() as Calendar
    cal.add(Calendar.DATE, days)
    return cal
}

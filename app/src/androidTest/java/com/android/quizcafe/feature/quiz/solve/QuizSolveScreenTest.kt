package com.android.quizcafe.feature.quiz.solve

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.android.quizcafe.core.ui.util.TestTags
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveEffect
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveIntent
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuizSolveScreenTest {

    // 컴포즈 테스트 환경 설정 규칙
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var fakeViewModel: FakeQuizSolveViewModel

    @Before
    fun setUp() {
        fakeViewModel = FakeQuizSolveViewModel()
    }

    // 실제 앱에서는 ViewModel에서 이 로직을 처리하지만 테스트에서는 onIntent 람다에서 직접 상태를 변경하여 ViewModel의 동작 처리
    @Test
    fun whenBackPressed_showsExitDialog_viaViewModelEffect() = runTest {
        var wasNavigateBackCalled = false

        composeTestRule.setContent {
            QuizSolveRoute(
                quizBookId = 1L,
                navigateToBack = { wasNavigateBackCalled = true },
                navigateToQuizBookSolvingResult = {},
                viewModel = fakeViewModel
            )
        }

        composeTestRule.onNodeWithTag(TestTags.QuizSolve.EXIT_DIALOG).assertDoesNotExist()

        Espresso.pressBack()
//        Thread.sleep(2000)  // 디버깅용
        assertTrue(fakeViewModel.receivedIntents.any { it is QuizSolveIntent.OnBackClick })

        // ViewModel 동작 시뮬레이션 및 UI 검증
        fakeViewModel.emitEffect(QuizSolveEffect.ShowExitDialog)
        composeTestRule.onNodeWithTag(TestTags.QuizSolve.EXIT_DIALOG).assertIsDisplayed()

        // 다이얼로그 내 버튼 클릭 및 검증
        composeTestRule.onNodeWithTag(TestTags.QuizSolve.EXIT_DIALOG_EXIT_SAVE_BUTTON).performClick()
        assertTrue(fakeViewModel.receivedIntents.any { it is QuizSolveIntent.ExitWithSave })
    }
}

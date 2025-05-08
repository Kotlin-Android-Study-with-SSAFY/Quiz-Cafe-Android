package com.android.quizcafe.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.outlineDark
import com.android.quizcafe.core.ui.QuizCafeTopAppBar
import com.android.quizcafe.core.ui.TopAppBarTitle

data class QuizHistory(
    val time: String,
    val title: String,
    val result: Int,
    val totalProblems: Int
)

val historyQizeList = listOf(
    QuizHistory("30분 전", "성준이의 운영체제", 16, 20),
    QuizHistory("16시간 전", "성민이의 네트워크", 16, 20),
    QuizHistory("04/01", "재용이의 안드로이드", 16, 20)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // 앱바 사용: title 반드시 전달
        QuizCafeTopAppBar(
            title = TopAppBarTitle.Text("Quiz Cafe"),
            alignTitleToStart = true
        )

        // 구분선
        HorizontalDivider(
            thickness = 1.dp
        )

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // "풀이 기록" Title & 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "풀이기록"
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "앞으로 이동"
                )
            }

            // "풀이 기록" 요소
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
                    .height(200.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(historyQizeList.size) { history ->
                        QuizHistoryCard(history = historyQizeList[history])
                    }
                }
            }

            // 4가지 grid 요소
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    FeatureCard(
                        title = "문제 풀기",
                        description = "혼자 원하는 카테코리를 선택해서 학습해요.",
                        backgroundColor = Color(185, 234, 217, 255)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureCard(
                        title = "1:1 퀴즈",
                        description = "친구 또는 모르는 사람과 퀴즈 대결을 진행해요.",
                        backgroundColor = Color(255, 247, 179, 255)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    FeatureCard(
                        title = "실시간 대결",
                        description = "여럿이서 실시간으로 퀴즈 대결을 진행해요.",
                        backgroundColor = Color(255, 201, 201, 255)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureCard(
                        title = "문제 생성",
                        description = "내가 원하는 문제들을 모아 문제집을 만들어요.",
                        backgroundColor = Color(216, 199, 255, 255)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuizHistoryCard(history: QuizHistory) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(249, 249, 249, 255), RoundedCornerShape(4.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(history.time, color = outlineDark)
        Text(history.title)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "결과 : ${history.result}/${history.totalProblems}"
            )
        }
    }
}

@Composable
fun RowScope.FeatureCard(
    title: String,
    description: String,
    backgroundColor: Color,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .border(1.dp, backgroundColor, RoundedCornerShape(15.dp))
            .background(backgroundColor, RoundedCornerShape(15.dp))
            .padding(16.dp),
    ) {
        Text(title)
        Text(description)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painterResource(R.drawable.quiz_unfill),
                contentDescription = "$title 이동"
            )
        }
    }
}

@Composable
fun MainScreenWithBottomBar() {
    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth(),
        bottomBar = {
            BottomAppBar(containerColor = Color(176, 202, 255, 255)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Filled.Home, contentDescription = "퀴즈 홈")
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            painterResource(R.drawable.star_unfill),
                            contentDescription = "북마크"
                        )
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Outlined.Person, contentDescription = "프로필")
                    }
                }
            }
        }
    ) { innerPadding ->
        // 실제 화면 내용은 여기서 innerPadding을 적용해서 그립니다.
        MainScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(modifier = Modifier)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenWithBottomBarPreview() {
    MainScreenWithBottomBar()
}

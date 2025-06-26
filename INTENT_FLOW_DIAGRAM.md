# QuizSolve Intent Flow Diagram

## 개선된 Intent 구조

### 1. Initialization & Data Loading
```
Initialize(quizBookId) 
├── loadQuizBook() → LoadQuizBookSuccess(quizBook)
└── getQuizBookLocalId() → SetQuizBookLocalId(localId) → LoadQuizBookGradeSuccess(grade)
```

### 2. User Interaction
```
SelectAnswer(option) → Update MCQ State
UpdateSubjectiveAnswer(answer) → Update Subjective State
```

### 3. Navigation
```
NavigateBack → NavigatePopBack Effect
NavigateToNextQuestion → SubmitCurrentAnswer → GradeQuizSuccess
NavigateToPreviousQuestion → getQuizAnswer() → GradeQuizSuccess
NavigateToResult → SubmitQuizBookSuccess → NavigateToQuizBookSolvingResult Effect
```

### 4. Quiz Submission & Grading
```
SubmitCurrentAnswer → saveQuizToLocal() → GradeQuizSuccess
SubmitAllAnswers → submitQuizAnswer() → SubmitQuizBookSuccess
HandleError(message) → ShowErrorDialog Effect
```

### 5. Timer
```
UpdateTimer → Update Timer State (every 1 second)
```

## Intent 분류 및 개선사항

### ✅ 개선된 점들:

1. **명확한 네이밍**
   - `LoadQuizBook` → `Initialize`
   - `SelectOption` → `SelectAnswer`
   - `UpdatedSubjectiveAnswer` → `UpdateSubjectiveAnswer`
   - `OnBackClick` → `NavigateBack`
   - `TickTime` → `UpdateTimer`

2. **논리적 그룹핑**
   - **Initialization & Data Loading**: 초기화 및 데이터 로딩 관련
   - **User Interaction**: 사용자 입력 관련
   - **Navigation**: 네비게이션 관련
   - **Quiz Submission & Grading**: 퀴즈 제출 및 채점 관련
   - **Timer**: 타이머 관련

3. **의도 명확화**
   - `SubmitNext` → `NavigateToNextQuestion`
   - `SubmitPrev` → `NavigateToPreviousQuestion`
   - `SubmitAnswer` → `SubmitAllAnswers`
   - `GradeQuizError` → `HandleError`

4. **일관성 있는 네이밍**
   - 모든 네비게이션 관련: `Navigate*`
   - 모든 제출 관련: `Submit*`
   - 모든 로딩 관련: `Load*` 또는 `Initialize`

## 전체 흐름도

```
[Screen Load] 
    ↓
Initialize(quizBookId)
    ↓
├── LoadQuizBookSuccess → Update UI State
└── SetQuizBookLocalId → LoadQuizBookGradeSuccess → Update UI State
    ↓
[User Interaction]
    ↓
├── SelectAnswer → Update MCQ State
├── UpdateSubjectiveAnswer → Update Subjective State
└── UpdateTimer → Update Timer State
    ↓
[Navigation Actions]
    ↓
├── NavigateBack → NavigatePopBack Effect
├── NavigateToNextQuestion → SubmitCurrentAnswer → GradeQuizSuccess
├── NavigateToPreviousQuestion → getQuizAnswer() → GradeQuizSuccess
└── NavigateToResult → SubmitQuizBookSuccess → NavigateToQuizBookSolvingResult Effect
    ↓
[Error Handling]
    ↓
HandleError → ShowErrorDialog Effect
```

## 사용 예시

### 1. 퀴즈북 초기화
```kotlin
sendIntent(QuizSolveIntent.Initialize(quizBookId))
```

### 2. 답안 선택
```kotlin
sendIntent(QuizSolveIntent.SelectAnswer(option))
sendIntent(QuizSolveIntent.UpdateSubjectiveAnswer(answer))
```

### 3. 네비게이션
```kotlin
sendIntent(QuizSolveIntent.NavigateBack)
sendIntent(QuizSolveIntent.NavigateToNextQuestion)
sendIntent(QuizSolveIntent.NavigateToPreviousQuestion)
```

### 4. 퀴즈 제출
```kotlin
sendIntent(QuizSolveIntent.SubmitCurrentAnswer)
sendIntent(QuizSolveIntent.SubmitAllAnswers)
```

### 5. 에러 처리
```kotlin
sendIntent(QuizSolveIntent.HandleError("에러 메시지"))
```

## 장점

1. **가독성 향상**: 명확한 네이밍으로 코드 이해도 증가
2. **유지보수성**: 논리적 그룹핑으로 관련 기능 찾기 쉬움
3. **확장성**: 새로운 기능 추가 시 적절한 그룹에 배치 가능
4. **일관성**: 일관된 네이밍 컨벤션으로 코드 품질 향상
5. **디버깅**: 명확한 의도로 디버깅 시 추적 용이 
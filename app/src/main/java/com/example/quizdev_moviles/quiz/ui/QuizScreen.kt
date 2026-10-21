package com.example.quizdev_moviles.quiz.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuizScreen(
    category: String,
    count: Int,
    onFinishQuiz: (score: Int, total: Int, correct: Int, maxStreak: Int, category: String, count: Int) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: GameViewModel = viewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    if (state.isFinished && state.questions.isNotEmpty()) {
        onFinishQuiz(
            state.score,
            state.questions.size,
            state.correctCount,
            state.maxStreak,
            category,
            count
        )
    }

    QuizContent(
        state = state,
        onOptionSelected = { index ->
            viewModel.onOptionSelected(index)
        },
        onNextClicked = {
            viewModel.onNextClicked()
        }
    )
}

package com.example.quizdev_moviles.quiz.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Composable con estado (QuizPage) que obtiene el GameViewModel, recolecta el estado
 * con collectAsStateWithLifecycle() y conecta los eventos a QuizContent.
 */
@Composable
fun QuizScreen(
    category: String,
    onFinishQuiz: (score: Int, total: Int) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: GameViewModel = viewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    if (state.isFinished && state.questions.isNotEmpty()) {
        onFinishQuiz(state.score, state.questions.size)
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

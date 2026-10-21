package com.example.quizdev_moviles.quiz.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizdev_moviles.R
import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.Difficulty
import com.example.quizdev_moviles.data.model.Question

/**
 * Composable sin estado que muestra el contenido de la pantalla de preguntas.
 * Recibe el estado actual (GameUiState), un evento onOptionSelected al elegir opción
 * y un evento onNextClicked al pulsar el botón de siguiente.
 */
@Composable
fun QuizContent(
    state: GameUiState,
    onOptionSelected: (Int) -> Unit,
    onNextClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.isLoading || state.questions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.loading_questions))
            }
        } else {
            val currentQuestion = state.questions[state.currentIndex]
            val total = state.questions.size
            val progress = (state.currentIndex + 1).toFloat() / total.toFloat()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.question_progress, state.currentIndex + 1, total),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.score_display, state.score),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.streak_display, state.streak),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentQuestion.text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    currentQuestion.options.forEachIndexed { index, optionText ->
                        val isSelected = state.selectedOption == index
                        val isCorrect = index == currentQuestion.correctIndex
                        OptionCard(
                            text = optionText,
                            isSelected = isSelected,
                            isCorrect = isCorrect,
                            isAnswered = state.isAnswered,
                            onClick = { onOptionSelected(index) }
                        )
                    }
                }

                Button(
                    onClick = onNextClicked,
                    enabled = state.isAnswered,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val buttonText = if (state.currentIndex < total - 1) {
                        stringResource(R.string.next_button)
                    } else {
                        stringResource(R.string.finish_button)
                    }
                    Text(text = buttonText)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizContentPreview() {
    val sampleQuestion = Question(
        id = "1",
        text = "¿Cuál es la palabra reservada para declarar una variable de solo lectura en Kotlin?",
        options = listOf("var", "val", "let"),
        correctIndex = 1,
        category = Category.KOTLIN,
        difficulty = Difficulty.EASY
    )
    val previewState = GameUiState(
        questions = listOf(sampleQuestion),
        currentIndex = 0,
        isLoading = false
    )
    QuizContent(
        state = previewState,
        onOptionSelected = {},
        onNextClicked = {}
    )
}

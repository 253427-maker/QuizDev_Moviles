package com.example.quizdev_moviles.quiz.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizScreen(
    category: String,
    onFinishQuiz: (score: Int, total: Int) -> Unit,
    onBack: () -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(1) }
    val totalQuestions = 5
    var selectedOption by remember { mutableStateOf<Int?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top bar / progress
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categoría: $category",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$currentQuestionIndex / $totalQuestions",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { currentQuestionIndex.toFloat() / totalQuestions.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Question content
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Cuál es la forma correcta de declarar una variable de solo lectura en Kotlin?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                val options = listOf("val x = 10", "var x = 10", "let x = 10", "const x = 10")
                options.forEachIndexed { index, option ->
                    val isSelected = selectedOption == index
                    OutlinedButton(
                        onClick = { selectedOption = index },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = option,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Bottom action button
            Button(
                onClick = {
                    if (currentQuestionIndex < totalQuestions) {
                        currentQuestionIndex++
                        selectedOption = null
                    } else {
                        // Dummy score for scaffolding phase 2
                        onFinishQuiz(4, totalQuestions)
                    }
                },
                enabled = selectedOption != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (currentQuestionIndex < totalQuestions) "Siguiente Pregunta" else "Finalizar Partida")
            }
        }
    }
}

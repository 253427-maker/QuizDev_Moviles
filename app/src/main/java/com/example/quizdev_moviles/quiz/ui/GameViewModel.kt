package com.example.quizdev_moviles.quiz.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.Difficulty
import com.example.quizdev_moviles.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar la lógica de negocio y el estado de la partida (GameUiState).
 * Recibe la categoría y la cantidad de preguntas mediante SavedStateHandle,
 * selecciona preguntas al azar sin repetir, calcula puntuaciones, rachas y actualiza el estado.
 */
class GameViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val repository = QuizRepository(application)

    private val categoryArg: String = savedStateHandle.get<String>("category") ?: "KOTLIN"
    private val countArg: Int = savedStateHandle.get<Int>("count") ?: 5

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        loadGameQuestions()
    }

    /**
     * Carga las preguntas del repositorio, filtra por categoría si corresponde,
     * selecciona una cantidad aleatoria sin repetir y establece el estado inicial del juego.
     */
    private fun loadGameQuestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val all = repository.allQuestions.first()
                val targetCategory = try {
                    Category.valueOf(categoryArg.uppercase())
                } catch (e: Exception) {
                    null
                }

                val filtered = if (targetCategory != null && categoryArg.uppercase() != "MIXTO") {
                    all.filter { it.category == targetCategory }
                } else {
                    all
                }

                val count = countArg.coerceIn(3, 10)
                val selected = filtered.shuffled().take(count)

                _uiState.update {
                    it.copy(
                        questions = selected,
                        currentIndex = 0,
                        isLoading = false,
                        isFinished = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Procesa la opción seleccionada por el usuario, calcula la puntuación basándose en
     * la dificultad de la pregunta y el sistema de rachas, y marca la pregunta como respondida.
     */
    fun onOptionSelected(index: Int) {
        val currentState = _uiState.value
        if (currentState.isAnswered || currentState.questions.isEmpty()) return

        val currentQuestion = currentState.questions[currentState.currentIndex]
        val isCorrect = index == currentQuestion.correctIndex

        var basePoints = 0
        if (isCorrect) {
            basePoints = when (currentQuestion.difficulty) {
                Difficulty.EASY -> 10
                Difficulty.MEDIUM -> 20
                Difficulty.HARD -> 30
            }
        }

        val newStreak = if (isCorrect) currentState.streak + 1 else 0
        var bonusPoints = 0
        if (isCorrect && newStreak >= 2) {
            bonusPoints = 10
        }

        val totalEarned = if (isCorrect) basePoints + bonusPoints else 0
        val newScore = currentState.score + totalEarned
        val newCorrectCount = if (isCorrect) currentState.correctCount + 1 else currentState.correctCount
        val newMaxStreak = maxOf(currentState.maxStreak, newStreak)

        _uiState.update {
            it.copy(
                selectedOption = index,
                score = newScore,
                correctCount = newCorrectCount,
                streak = newStreak,
                maxStreak = newMaxStreak,
                lastAnswerPoints = totalEarned,
                isAnswered = true
            )
        }
    }

    /**
     * Avanza a la siguiente pregunta del cuestionario o finaliza la partida si era la última.
     */
    fun onNextClicked() {
        val currentState = _uiState.value
        if (!currentState.isAnswered) return

        val nextIndex = currentState.currentIndex + 1
        if (nextIndex < currentState.questions.size) {
            _uiState.update {
                it.copy(
                    currentIndex = nextIndex,
                    selectedOption = null,
                    isAnswered = false,
                    lastAnswerPoints = 0
                )
            }
        } else {
            _uiState.update {
                it.copy(isFinished = true)
            }
        }
    }
}

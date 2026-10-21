package com.example.quizdev_moviles.questions.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizdev_moviles.data.model.AddQuestionResult
import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.Difficulty
import com.example.quizdev_moviles.data.model.Question
import com.example.quizdev_moviles.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class NewQuestionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuizRepository(application)

    private val _uiState = MutableStateFlow(NewQuestionUiState())
    val uiState: StateFlow<NewQuestionUiState> = _uiState.asStateFlow()

    fun onTextChanged(newText: String) {
        if (newText.length <= 120) {
            _uiState.update { it.copy(text = newText, errorMessage = null) }
        }
    }

    fun onOptionChanged(index: Int, value: String) {
        val currentOptions = _uiState.value.options.toMutableList()
        if (index in currentOptions.indices) {
            currentOptions[index] = value
            _uiState.update { it.copy(options = currentOptions, errorMessage = null) }
        }
    }

    fun onCorrectOptionChanged(index: Int) {
        _uiState.update { it.copy(correctIndex = index, errorMessage = null) }
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(category = category) }
    }

    fun onDifficultySelected(difficulty: Difficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    fun onSaveClicked(errorMessageComplete: String, errorDuplicate: String, onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.text.isBlank() || state.options.any { it.isBlank() }) {
            _uiState.update { it.copy(errorMessage = errorMessageComplete) }
            return
        }

        viewModelScope.launch {
            val question = Question(
                id = UUID.randomUUID().toString(),
                text = state.text.trim(),
                options = state.options.map { it.trim() },
                correctIndex = state.correctIndex,
                category = state.category,
                difficulty = state.difficulty,
                isUserCreated = true
            )

            val result = repository.addUserQuestion(question)
            when (result) {
                AddQuestionResult.DUPLICATE -> {
                    _uiState.update { it.copy(errorMessage = errorDuplicate) }
                }
                AddQuestionResult.SAVED -> {
                    _uiState.update { it.copy(isSaved = true, errorMessage = null) }
                    onSuccess()
                }
            }
        }
    }
}

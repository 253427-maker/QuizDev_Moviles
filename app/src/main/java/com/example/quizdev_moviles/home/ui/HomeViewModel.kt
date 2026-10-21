package com.example.quizdev_moviles.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuizRepository(application)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repository.bestScore.collect { score ->
                    _uiState.update { it.copy(bestScore = score) }
                }
            } catch (e: Exception) {
            }
        }

        viewModelScope.launch {
            try {
                repository.history.collect { hist ->
                    _uiState.update { it.copy(history = hist) }
                }
            } catch (e: Exception) {
            }
        }

        viewModelScope.launch {
            try {
                repository.allQuestions
                    .catch { _uiState.update { state -> state.copy(questionsLoadFailed = true) } }
                    .collect { questions ->
                        if (questions.isEmpty()) {
                            _uiState.update { it.copy(questionsLoadFailed = true) }
                        } else {
                            _uiState.update { it.copy(questionsLoadFailed = false) }
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(questionsLoadFailed = true) }
            }
        }
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onQuestionCountChanged(count: Int) {
        _uiState.update { it.copy(questionCount = count) }
    }
}

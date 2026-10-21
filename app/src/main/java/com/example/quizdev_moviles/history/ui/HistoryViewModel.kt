package com.example.quizdev_moviles.history.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizdev_moviles.data.model.GameResult
import com.example.quizdev_moviles.data.repository.QuizRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel que expone el historial de partidas almacenadas en DataStore para la pantalla de historial.
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuizRepository(application)

    val history: StateFlow<List<GameResult>> = repository.history.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}

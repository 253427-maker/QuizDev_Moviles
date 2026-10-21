package com.example.quizdev_moviles.home.ui

import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.GameResult

data class HomeUiState(
    val bestScore: Int = 0,
    val history: List<GameResult> = emptyList(),
    val selectedCategory: Category = Category.KOTLIN,
    val questionCount: Int = 5,
    val questionsLoadFailed: Boolean = false
)

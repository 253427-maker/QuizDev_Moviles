package com.example.quizdev_moviles.quiz.ui

import com.example.quizdev_moviles.data.model.Question

data class GameUiState(
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOption: Int? = null,
    val score: Int = 0,
    val correctCount: Int = 0,
    val streak: Int = 0,
    val maxStreak: Int = 0,
    val lastAnswerPoints: Int = 0,
    val isAnswered: Boolean = false,
    val isLoading: Boolean = true,
    val isFinished: Boolean = false
)

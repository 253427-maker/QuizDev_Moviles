package com.example.quizdev_moviles.questions.ui

import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.Difficulty

data class NewQuestionUiState(
    val text: String = "",
    val options: List<String> = listOf("", "", ""),
    val correctIndex: Int = 0,
    val category: Category = Category.KOTLIN,
    val difficulty: Difficulty = Difficulty.EASY,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

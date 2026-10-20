package com.example.quizdev_moviles.data.model

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val category: Category,
    val difficulty: Difficulty,
    val isUserCreated: Boolean = false
)

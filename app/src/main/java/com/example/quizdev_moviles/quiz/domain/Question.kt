package com.example.quizdev_moviles.quiz.domain

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val category: String,
    val difficulty: String = "Fácil"
)

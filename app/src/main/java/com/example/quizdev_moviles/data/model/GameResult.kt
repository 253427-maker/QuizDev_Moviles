package com.example.quizdev_moviles.data.model

data class GameResult(
    val dateMillis: Long,
    val category: Category,
    val correct: Int,
    val total: Int,
    val score: Int
)

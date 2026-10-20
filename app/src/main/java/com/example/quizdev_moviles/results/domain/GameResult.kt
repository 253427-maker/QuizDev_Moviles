package com.example.quizdev_moviles.results.domain

data class GameResult(
    val id: String,
    val date: Long,
    val score: Int,
    val total: Int,
    val category: String,
    val correctAnswers: Int
)

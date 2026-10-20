package com.example.quizdev_moviles.data.repository

import android.content.Context
import com.example.quizdev_moviles.data.model.AddQuestionResult
import com.example.quizdev_moviles.data.model.GameResult
import com.example.quizdev_moviles.data.model.Question
import com.example.quizdev_moviles.data.source.QuestionsLocalDataSource
import com.example.quizdev_moviles.data.source.UserLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class QuizRepository(context: Context) {
    private val questionsLocalDataSource = QuestionsLocalDataSource(context)
    private val userLocalDataSource = UserLocalDataSource(context)

    val bestScore: Flow<Int> = userLocalDataSource.bestScore

    val history: Flow<List<GameResult>> = userLocalDataSource.history

    val allQuestions: Flow<List<Question>> = combine(
        flow { emit(questionsLocalDataSource.loadQuestions()) },
        userLocalDataSource.userQuestions
    ) { seedList, userList ->
        seedList + userList
    }

    suspend fun addUserQuestion(question: Question): AddQuestionResult {
        val normalizedNewText = question.text.replace("\\s".toRegex(), "").lowercase()
        val seedList = questionsLocalDataSource.loadQuestions()
        val userList = userLocalDataSource.userQuestions.first()
        val allCurrent = seedList + userList

        val exists = allCurrent.any { q ->
            q.text.replace("\\s".toRegex(), "").lowercase() == normalizedNewText
        }

        if (exists) {
            return AddQuestionResult.DUPLICATE
        }

        userLocalDataSource.saveUserQuestion(question)
        return AddQuestionResult.SAVED
    }

    suspend fun saveGameResult(result: GameResult) {
        userLocalDataSource.saveGameResult(result)
    }
}

package com.example.quizdev_moviles.data.source

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.Difficulty
import com.example.quizdev_moviles.data.model.GameResult
import com.example.quizdev_moviles.data.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore(name = "quizdev_prefs")

class UserLocalDataSource(private val context: Context) {
    private object PreferencesKeys {
        val USER_QUESTIONS = stringPreferencesKey("user_questions")
        val HISTORY = stringPreferencesKey("history")
        val BEST_SCORE = intPreferencesKey("best_score")
    }

    val userQuestions: Flow<List<Question>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.USER_QUESTIONS] ?: "[]"
        parseQuestionsJson(jsonString)
    }

    val history: Flow<List<GameResult>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.HISTORY] ?: "[]"
        parseHistoryJson(jsonString)
    }

    val bestScore: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.BEST_SCORE] ?: 0
    }

    suspend fun saveUserQuestion(question: Question) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.USER_QUESTIONS] ?: "[]"
            val list = parseQuestionsJson(currentJson).toMutableList()
            list.add(question)
            preferences[PreferencesKeys.USER_QUESTIONS] = serializeQuestions(list)
        }
    }

    suspend fun saveGameResult(result: GameResult) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.HISTORY] ?: "[]"
            val list = parseHistoryJson(currentJson).toMutableList()
            list.add(0, result)
            val sortedList = list.sortedByDescending { it.dateMillis }.take(20)
            preferences[PreferencesKeys.HISTORY] = serializeHistory(sortedList)

            val currentBest = preferences[PreferencesKeys.BEST_SCORE] ?: 0
            if (result.score > currentBest) {
                preferences[PreferencesKeys.BEST_SCORE] = result.score
            }
        }
    }

    private fun parseQuestionsJson(jsonString: String): List<Question> {
        val list = mutableListOf<Question>()
        try {
            val arr = JSONArray(jsonString)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val id = obj.getString("id")
                val text = obj.getString("text")
                val optionsArr = obj.getJSONArray("options")
                val options = mutableListOf<String>()
                for (j in 0 until optionsArr.length()) {
                    options.add(optionsArr.getString(j))
                }
                val correctIndex = obj.getInt("correctIndex")
                val category = Category.valueOf(obj.getString("category"))
                val difficulty = Difficulty.valueOf(obj.getString("difficulty"))
                val isUserCreated = obj.optBoolean("isUserCreated", true)
                list.add(
                    Question(
                        id = id,
                        text = text,
                        options = options,
                        correctIndex = correctIndex,
                        category = category,
                        difficulty = difficulty,
                        isUserCreated = isUserCreated
                    )
                )
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return list
    }

    private fun serializeQuestions(questions: List<Question>): String {
        val arr = JSONArray()
        for (q in questions) {
            val obj = JSONObject()
            obj.put("id", q.id)
            obj.put("text", q.text)
            val optionsArr = JSONArray()
            for (opt in q.options) {
                optionsArr.put(opt)
            }
            obj.put("options", optionsArr)
            obj.put("correctIndex", q.correctIndex)
            obj.put("category", q.category.name)
            obj.put("difficulty", q.difficulty.name)
            obj.put("isUserCreated", q.isUserCreated)
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun parseHistoryJson(jsonString: String): List<GameResult> {
        val list = mutableListOf<GameResult>()
        try {
            val arr = JSONArray(jsonString)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val dateMillis = obj.getLong("dateMillis")
                val category = Category.valueOf(obj.getString("category"))
                val correct = obj.getInt("correct")
                val total = obj.getInt("total")
                val score = obj.getInt("score")
                list.add(
                    GameResult(
                        dateMillis = dateMillis,
                        category = category,
                        correct = correct,
                        total = total,
                        score = score
                    )
                )
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return list
    }

    private fun serializeHistory(history: List<GameResult>): String {
        val arr = JSONArray()
        for (h in history) {
            val obj = JSONObject()
            obj.put("dateMillis", h.dateMillis)
            obj.put("category", h.category.name)
            obj.put("correct", h.correct)
            obj.put("total", h.total)
            obj.put("score", h.score)
            arr.put(obj)
        }
        return arr.toString()
    }
}

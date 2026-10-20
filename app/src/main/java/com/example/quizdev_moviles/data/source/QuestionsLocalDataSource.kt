package com.example.quizdev_moviles.data.source

import android.content.Context
import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.Difficulty
import com.example.quizdev_moviles.data.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class QuestionsLocalDataSource(private val context: Context) {
    suspend fun loadQuestions(): List<Question> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("questions.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<Question>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getString("id")
                val text = obj.getString("text")
                val optionsArray = obj.getJSONArray("options")
                val options = mutableListOf<String>()
                for (j in 0 until optionsArray.length()) {
                    options.add(optionsArray.getString(j))
                }
                val correctIndex = obj.getInt("correctIndex")
                val category = Category.valueOf(obj.getString("category"))
                val difficulty = Difficulty.valueOf(obj.getString("difficulty"))
                val isUserCreated = obj.optBoolean("isUserCreated", false)
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
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}

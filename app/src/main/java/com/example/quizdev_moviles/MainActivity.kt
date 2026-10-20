package com.example.quizdev_moviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quizdev_moviles.navigation.QuizDevNavGraph
import com.example.quizdev_moviles.ui.theme.QuizDev_MovilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizDev_MovilesTheme {
                QuizDevNavGraph()
            }
        }
    }
}

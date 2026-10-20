package com.example.quizdev_moviles.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizdev_moviles.home.ui.HomeScreen
import com.example.quizdev_moviles.quiz.ui.QuizScreen
import com.example.quizdev_moviles.results.ui.ResultsScreen
import com.example.quizdev_moviles.questions.ui.NewQuestionScreen
import com.example.quizdev_moviles.history.ui.HistoryScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Quiz : Screen("quiz/{category}") {
        fun createRoute(category: String) = "quiz/$category"
    }
    object Results : Screen("results/{score}/{total}/{category}") {
        fun createRoute(score: Int, total: Int, category: String) = "results/$score/$total/$category"
    }
    object NewQuestion : Screen("new_question")
    object History : Screen("history")
}

@Composable
fun QuizDevNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartQuiz = { category ->
                    navController.navigate(Screen.Quiz.createRoute(category))
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                onNavigateToNewQuestion = {
                    navController.navigate(Screen.NewQuestion.route)
                }
            )
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Kotlin"
            QuizScreen(
                category = category,
                onFinishQuiz = { score, total ->
                    navController.navigate(Screen.Results.createRoute(score, total, category)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Results.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType },
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 5
            val category = backStackEntry.arguments?.getString("category") ?: "Kotlin"

            ResultsScreen(
                score = score,
                total = total,
                category = category,
                onHome = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
                onRetry = {
                    navController.navigate(Screen.Quiz.createRoute(category)) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.NewQuestion.route) {
            NewQuestionScreen(
                onQuestionSaved = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

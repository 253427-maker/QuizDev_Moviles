package com.example.quizdev_moviles.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.quizdev_moviles.R
import com.example.quizdev_moviles.core.ui.CategorySelector
import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.GameResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onCategorySelected: (Category) -> Unit,
    onQuestionCountChanged: (Int) -> Unit,
    onStartGame: (Category, Int) -> Unit,
    onNavigateToNewQuestion: () -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.home_title)) },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(3)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = stringResource(R.string.history_icon_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BestScoreCard(bestScore = state.bestScore)
            }

            item {
                CategorySelector(
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }

            item {
                QuestionCountSlider(
                    count = state.questionCount,
                    onCountChanged = onQuestionCountChanged
                )
            }

            item {
                PlayButtonSection(
                    loadFailed = state.questionsLoadFailed,
                    onPlayClick = { onStartGame(state.selectedCategory, state.questionCount) },
                    onNewQuestionClick = onNavigateToNewQuestion
                )
            }

            item {
                Text(
                    text = stringResource(R.string.recent_games_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (state.history.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.empty_history),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(state.history) { result ->
                    GameResultItem(result = result)
                }
            }
        }
    }
}

@Composable
private fun BestScoreCard(bestScore: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.best_score_label, bestScore),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun QuestionCountSlider(
    count: Int,
    onCountChanged: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.question_count_label, count),
            style = MaterialTheme.typography.bodyLarge
        )
        Slider(
            value = count.toFloat(),
            onValueChange = { onCountChanged(it.toInt()) },
            valueRange = 3f..10f,
            steps = 6
        )
    }
}

@Composable
private fun PlayButtonSection(
    loadFailed: Boolean,
    onPlayClick: () -> Unit,
    onNewQuestionClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onPlayClick,
            enabled = !loadFailed,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (loadFailed) stringResource(R.string.questions_load_failed) else stringResource(R.string.play_button)
            )
        }

        TextButton(onClick = onNewQuestionClick) {
            Text(text = stringResource(R.string.add_question_button))
        }
    }
}

@Composable
private fun GameResultItem(result: GameResult) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = result.category.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.score_format, result.score, result.correct, result.total),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = stringResource(R.string.date_format),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

package com.example.quizdev_moviles.questions.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizdev_moviles.R
import com.example.quizdev_moviles.core.ui.CategorySelector
import com.example.quizdev_moviles.data.model.Category
import com.example.quizdev_moviles.data.model.Difficulty

@Composable
fun NewQuestionScreen(
    onQuestionSaved: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: NewQuestionViewModel = viewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    val errorComplete = stringResource(R.string.error_complete_fields)
    val errorDuplicate = stringResource(R.string.error_question_duplicate)

    NewQuestionContent(
        state = state,
        onTextChanged = { viewModel.onTextChanged(it) },
        onOptionChanged = { index, value -> viewModel.onOptionChanged(index, value) },
        onCorrectOptionChanged = { index -> viewModel.onCorrectOptionChanged(index) },
        onCategorySelected = { category -> viewModel.onCategorySelected(category) },
        onDifficultySelected = { difficulty -> viewModel.onDifficultySelected(difficulty) },
        onSaveClicked = {
            viewModel.onSaveClicked(errorComplete, errorDuplicate, onQuestionSaved)
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewQuestionContent(
    state: NewQuestionUiState,
    onTextChanged: (String) -> Unit,
    onOptionChanged: (Int, String) -> Unit,
    onCorrectOptionChanged: (Int) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onDifficultySelected: (Difficulty) -> Unit,
    onSaveClicked: () -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.new_question_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                OutlinedTextField(
                    value = state.text,
                    onValueChange = onTextChanged,
                    label = { Text(text = stringResource(R.string.question_text_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text(text = "${state.text.length}/120")
                    }
                )
            }

            items(3) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = state.correctIndex == index,
                        onClick = { onCorrectOptionChanged(index) }
                    )
                    OutlinedTextField(
                        value = state.options[index],
                        onValueChange = { onOptionChanged(index, it) },
                        label = { Text(text = stringResource(R.string.option_label, index + 1)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                CategorySelector(
                    selectedCategory = state.category,
                    onCategorySelected = onCategorySelected
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.difficulty_label),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Difficulty.entries.forEach { diff ->
                            val label = when (diff) {
                                Difficulty.EASY -> stringResource(R.string.difficulty_easy)
                                Difficulty.MEDIUM -> stringResource(R.string.difficulty_medium)
                                Difficulty.HARD -> stringResource(R.string.difficulty_hard)
                            }
                            FilterChip(
                                selected = state.difficulty == diff,
                                onClick = { onDifficultySelected(diff) },
                                label = { Text(text = label) }
                            )
                        }
                    }
                }
            }

            if (state.errorMessage != null) {
                item {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onSaveClicked,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.save_question_button))
                    }

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.cancel_button))
                    }
                }
            }
        }
    }
}

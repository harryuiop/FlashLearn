package com.example.seng303_assignment1.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.seng303_assignment1.model.FlashCard
import com.example.seng303_assignment1.viewModels.FlashCardViewModel
import com.example.seng303_assignment1.viewModels.EditFlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFlashCard(
    navController: NavController,
    flashCardIdParam: String,
    flashCardViewModel: FlashCardViewModel,
    editFlashCardViewModel: EditFlashCardViewModel
) {
    val question by editFlashCardViewModel.question.collectAsState()
    val answerOptions by editFlashCardViewModel.answerOptions.collectAsState()
    val selectedAnswerIndex by editFlashCardViewModel.selectedAnswerIndex.collectAsState()
    val reRenderIndex by editFlashCardViewModel.reRenderIndex.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val flashCardId = flashCardIdParam.toIntOrNull()
    val flashCardState by flashCardViewModel.selectedFlashCard.collectAsState(null)
    val flashCard: FlashCard? = flashCardState

    LaunchedEffect(reRenderIndex) {
        editFlashCardViewModel.resetState()
        if (flashCard == null) {
            flashCardViewModel.getFlashCardById(flashCardId)
        } else {
            editFlashCardViewModel.setDefaultValues(flashCard)
            editFlashCardViewModel.updateSelectedAnswerIndex(answerOptions.indexOfFirst { it.isCorrect })
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Update Question",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            flashCard?.let {
                OutlinedTextField(
                    value = question,
                    onValueChange = { editFlashCardViewModel.updateQuestion(it) },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            answerOptions.forEachIndexed { index, answer ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = selectedAnswerIndex == index,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                editFlashCardViewModel.updateSelectedAnswerIndex(index)
                                editFlashCardViewModel.updateCorrectAnswer(answerOptions[index])

                                for (nums in 0..<editFlashCardViewModel.answerOptions.value.size) {
                                    if (nums != index) {
                                        editFlashCardViewModel.setCorrectAnswerFalse(nums)
                                    }
                                }

                            } else if (selectedAnswerIndex == index) {
                                editFlashCardViewModel.updateSelectedAnswerIndex(-1)
                                editFlashCardViewModel.setCorrectAnswerFalse(index)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = answer.answerText,
                        onValueChange = { newAnswer ->
                            if (answerOptions[index].isCorrect) {
                                editFlashCardViewModel.updateAnswerOptions(index, newAnswer, true)
                            } else {
                                editFlashCardViewModel.updateAnswerOptions(index, newAnswer, false)
                            }
                        },
                        label = { Text("Answer option") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(
                    onClick = {
                        editFlashCardViewModel.addAnswerOption()
                    },
                    modifier = Modifier.offset((-5).dp)
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Add Answer Option"
                    )
                }
                Button(
                    onClick = {
                        if (answerOptions.size > 3) {
                            editFlashCardViewModel.removeAnswerOption()
                        } else {
                            showErrorDialog = true
                            errorMessage = "Question must contain at least three options"
                        }
                    },
                    modifier = Modifier.offset(5.dp)
                ) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Remove Answer Option"
                    )
                }
            }
        }

        Button(
            onClick = {

                var hasEmptyAnswer = false
                var chosenAnswer = false

                for (answer in answerOptions) {
                    if (answer.answerText.isEmpty()) {
                        hasEmptyAnswer = true
                        errorMessage = "Please fill out all answer options"
                        break
                    }
                }

                if (question.isEmpty()) {
                    hasEmptyAnswer = true
                    errorMessage = "Please fill out a question"
                }

                answerOptions.forEach { answer ->
                    if (answer.isCorrect) {
                        chosenAnswer = true
                    }
                }

                if (!chosenAnswer) {
                    hasEmptyAnswer = true
                    errorMessage = "Please confirm a correct answer"
                }

                if (!hasEmptyAnswer) {
                    flashCard?.let {
                        flashCardViewModel.editFlashCard(flashCardIdParam.toInt(),
                            FlashCard(
                                flashCardViewModel.generateFlashCardId(),
                                question,
                                answerOptions
                            )
                        )
                        editFlashCardViewModel.refreshQuestion()
                        editFlashCardViewModel.refreshSelectedIndex()
                        editFlashCardViewModel.refreshAnswerOptions()
                    }
                    navController.navigate("ViewFlashCards")
                } else {
                    showErrorDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Question")
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    Button(
                        onClick = {
                            showErrorDialog = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

    }
}


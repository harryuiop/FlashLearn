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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.seng303_assignment1.model.FlashCard
import com.example.seng303_assignment1.viewModels.FlashCardViewModel
import com.example.seng303_assignment1.viewModels.NewFlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFlashCard(
    navController: NavController,
    flashCardViewModel: FlashCardViewModel = viewModel(),
    newFlashCardViewModel: NewFlashCardViewModel = viewModel()
) {
    val question by newFlashCardViewModel.question.collectAsState()
    val answerOptions by newFlashCardViewModel.answerOptions.collectAsState()
    val selectedAnswerIndex by newFlashCardViewModel.selectedAnswerIndex.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    flashCardViewModel.getAllFlashCards()
    val flashCards: List<FlashCard> by flashCardViewModel.flashCard.collectAsState(emptyList())

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
                "Create Question",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = question,
                onValueChange = { newFlashCardViewModel.updateQuestion(it) },
                label = { Text("Question") },
                modifier = Modifier.fillMaxWidth()
            )

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
                                newFlashCardViewModel.updateSelectedAnswerIndex(index)
                                newFlashCardViewModel.updateCorrectAnswer(answerOptions[index])

                                for (nums in 0..<newFlashCardViewModel.answerOptions.value.size) {
                                    if (nums != index) {
                                        newFlashCardViewModel.setCorrectAnswerFalse(nums)
                                    }
                                }
                            } else if (selectedAnswerIndex == index) {
                                newFlashCardViewModel.updateSelectedAnswerIndex(-1)
                                newFlashCardViewModel.setCorrectAnswerFalse(index)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = answer.answerText,
                        onValueChange = { newAnswer ->
                            if (answerOptions[index].isCorrect) {
                                newFlashCardViewModel.updateAnswerOptions(index, newAnswer, true)
                            } else {
                                newFlashCardViewModel.updateAnswerOptions(index, newAnswer, false)
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
                        newFlashCardViewModel.addAnswerOption()
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
                        if (newFlashCardViewModel.answerOptions.value.size > 3) {
                            newFlashCardViewModel.removeAnswerOption()
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

                flashCards.forEach{ card ->
                    if (card.question == question) {
                        hasEmptyAnswer = true
                        errorMessage = "Flash card with this question already created"
                    }
                }

                if (!hasEmptyAnswer) {
                    flashCardViewModel.createFlashCard(question, answerOptions)
                    newFlashCardViewModel.refreshQuestion()
                    newFlashCardViewModel.refreshAnswerOptions()
                    newFlashCardViewModel.refreshSelectedIndex()
                    navController.navigate("ViewFlashCards")
                } else {
                    showErrorDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save and return")
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


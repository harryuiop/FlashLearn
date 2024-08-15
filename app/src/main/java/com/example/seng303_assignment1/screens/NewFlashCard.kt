package com.example.seng303_assignment1.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.seng303_assignment1.model.AnswerOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFlashCard(
    createFlashCardFn: (String, List<AnswerOption>) -> Unit,
    fetchQuestionFn: () -> String,
    updateAnswerOptionsFn: (Int, String, Boolean) -> Unit,
    updateQuestionFn: (String) -> Unit,
    fetchAnswerOptionsFn: () -> List<AnswerOption>,
    addAnswerOptionFn: (AnswerOption) -> Unit,
    updateCorrectAnswerFn: (AnswerOption) -> Unit,
    setCorrectAnswerFalseFn:(Int) -> Unit,
    navController: NavController
    ) {
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var selectedAnswer by remember { mutableStateOf<AnswerOption?>(null) }
    var selectedAnswerIndex by remember { mutableStateOf(-1) }
    val scrollState = rememberScrollState()


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
                "Add a new flash card",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fetchQuestionFn(),
                onValueChange = { updateQuestionFn(it) },
                label = { Text("Question") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            fetchAnswerOptionsFn().forEachIndexed { index, answer ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = selectedAnswerIndex == index,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedAnswerIndex = index
                                updateCorrectAnswerFn(fetchAnswerOptionsFn()[index])

                                for (nums in 0..<fetchAnswerOptionsFn().size) {
                                    if (nums != index) {
                                        setCorrectAnswerFalseFn(nums)
                                    }
                                }
                            } else if (selectedAnswerIndex == index) {
                                selectedAnswerIndex = -1
                                setCorrectAnswerFalseFn(index)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = answer.answerText,
                        onValueChange = { newAnswer ->
                            updateAnswerOptionsFn(index, newAnswer, false)
                        },
                        label = { Text("Answer option") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    addAnswerOptionFn(AnswerOption("", false))
                }
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add Answer Option"
                )
            }
        }

        Button(
            onClick = {
                val answerOptions = fetchAnswerOptionsFn()
                val question = fetchQuestionFn()

                var hasEmptyAnswer = false
                var choosenAnswer = false

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
                        choosenAnswer = true
                    }
                }

                if (!choosenAnswer) {
                    hasEmptyAnswer = true
                    errorMessage = "Please confirm a correct answer"
                }

                if (!hasEmptyAnswer) {
                        createFlashCardFn(fetchQuestionFn(), answerOptions)
                        navController.navigate("Home")
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


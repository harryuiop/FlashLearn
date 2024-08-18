package com.example.seng303_assignment1.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seng303_assignment1.model.FlashCard
import com.example.seng303_assignment1.model.GameRoundResult
import com.example.seng303_assignment1.viewModels.FlashCardViewModel
import com.example.seng303_assignment1.viewModels.PlayViewModel

@Composable
fun Play(
    navController: NavController,
    flashCardViewModel: FlashCardViewModel,
    playViewModel: PlayViewModel
) {
    flashCardViewModel.getAllFlashCards()
    val flashCards: List<FlashCard> by flashCardViewModel.flashCard.collectAsState(emptyList())
    var index by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf(-1) }
    var showErrorDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        playViewModel.resetGameResults()
    }

        if (flashCards.isNotEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = flashCards[index].question,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp)
                ) {
                    flashCards[index].answerOptions.forEachIndexed { optionIndex, option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = selectedAnswerIndex == optionIndex,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedAnswerIndex = optionIndex
                                    } else if (selectedAnswerIndex == optionIndex) {
                                        selectedAnswerIndex = -1
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(option.answerText)
                        }
                    }
                }

                Button(
                    onClick = {
                        if (selectedAnswerIndex != -1) {
                            if (index < flashCards.size - 1) {
                                if (flashCards[index].answerOptions[selectedAnswerIndex].isCorrect) {
                                    playViewModel.incrementCorrectAnswersTotal()
                                    playViewModel.addToGameResults(
                                        GameRoundResult(
                                            flashCards[index], true
                                        )
                                    )
                                    Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show()
                                } else {
                                    playViewModel.addToGameResults(
                                        GameRoundResult(
                                            flashCards[index], false
                                        )
                                    )
                                    Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT).show()
                                }
                                selectedAnswerIndex = -1
                                index++

                            } else {
                                navController.navigate("EndGame")
                            }
                        } else {
                            showErrorDialog = true
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Next")
                }

                if (showErrorDialog) {
                    AlertDialog(
                        onDismissRequest = { showErrorDialog = false },
                        title = { Text("No answer selected") },
                        text = { Text("Click on an answer's checkbox to select it as correct") },
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

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Question: ${index + 1} / ${flashCards.size}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }


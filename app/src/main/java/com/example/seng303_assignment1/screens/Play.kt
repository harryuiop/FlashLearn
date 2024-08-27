package com.example.seng303_assignment1.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@SuppressLint("RestrictedApi")
@Composable
fun Play(
    navController: NavController,
    flashCardViewModel: FlashCardViewModel,
    playViewModel: PlayViewModel
) {
    flashCardViewModel.getAllFlashCards()
    val flashCards: List<FlashCard> by flashCardViewModel.flashCard.collectAsState(emptyList())
    val reRenderIndex by playViewModel.reRenderIndex.collectAsState()
    val selectedAnswerIndex by playViewModel.selectedAnswerIndex.collectAsState()
    val shuffledFlashCards by playViewModel.shuffledFlashCards.collectAsState()
    val index by playViewModel.index.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(reRenderIndex) {
        if (!playViewModel.shuffled.value) {
            playViewModel.setShuffledFlashCards(flashCards.shuffled())
            playViewModel.setShuffledTrue()
            Log.e("shuffled", "true")
        }
    }

    if (shuffledFlashCards.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = shuffledFlashCards[index].question,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
            ) {
                shuffledFlashCards[index].answerOptions.forEachIndexed { optionIndex, option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = selectedAnswerIndex == optionIndex,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    playViewModel.updateSelectedAnswerIndex(optionIndex)
                                } else if (selectedAnswerIndex == optionIndex) {
                                    playViewModel.updateSelectedAnswerIndex(-1)
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
                        if (index <= shuffledFlashCards.size - 1) {
                            if (shuffledFlashCards[index].answerOptions[selectedAnswerIndex].isCorrect) {
                                playViewModel.incrementCorrectAnswersTotal()
                                playViewModel.addToGameResults(
                                    GameRoundResult(
                                        shuffledFlashCards[index], true
                                    )
                                )
                                Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show()
                            } else {
                                playViewModel.addToGameResults(
                                    GameRoundResult(
                                        shuffledFlashCards[index], false
                                    )
                                )
                                Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT).show()
                            }
                            playViewModel.updateSelectedAnswerIndex(-1)
                            if (index + 1 == shuffledFlashCards.size) {
                                navController.navigate("EndGame")
                            } else {
                                playViewModel.incrementIndex()
                            }
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
                    text = "Question: ${index + 1} / ${shuffledFlashCards.size}",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No Flashcards Created!",
                    modifier = Modifier.offset(0.dp, -(35).dp))
                Text(text = "Would you like to create one?")
                Button(onClick = { navController.navigate("NewFlashCard") }) {
                    Text("Create Flash Card")
                }
            }
        }
    }
}


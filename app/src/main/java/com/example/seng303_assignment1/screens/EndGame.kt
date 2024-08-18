package com.example.seng303_assignment1.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seng303_assignment1.model.GameRoundResult
import com.example.seng303_assignment1.viewModels.PlayViewModel

@Composable
fun EndGame(
    playViewModel: PlayViewModel,
    navController: NavController
) {
    val flashCards: List<GameRoundResult> = playViewModel.returnGameResults()
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)
        ) {
            Text(
                text = "Score: ${playViewModel.correctAnswersTotal} / ${flashCards.size}",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )

            Button(
                onClick = {
                    navController.navigate("Home")
                },
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Text("Return Home")
            }
        }

        LazyColumn {
            items(flashCards) { item ->
                Row(
                    Modifier.padding(start = 20.dp, top = 15.dp)
                ) {
                    Text(
                        text = "Question ${
                            flashCards.indexOfFirst
                            { question -> question.flashCard.question == item.flashCard.question } + 1
                        }:",
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = item.flashCard.question,
                            modifier = Modifier
                                .fillMaxHeight()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (item.correct) {
                            Icon(
                                Icons.Rounded.Done,
                                contentDescription = "Correct",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Green
                            )
                        } else {
                            Icon(
                                Icons.Rounded.Clear,
                                contentDescription = "Incorrect",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}
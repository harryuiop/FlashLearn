package com.example.seng303_assignment1.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seng303_assignment1.model.FlashCard
import com.example.seng303_assignment1.viewModels.FlashCardViewModel

@Composable
fun FlashCardList(navController: NavController, flashCardViewModel: FlashCardViewModel) {
    flashCardViewModel.getAllFlashCards()
    val flashCards: List<FlashCard> by flashCardViewModel.flashCard.collectAsState(emptyList())
    LazyColumn {
        items(flashCards) { flashCardItem ->
            FlashCardItem(navController = navController, flashCard = flashCardItem, flashCardViewModel)
        }
    }
}

@Composable
fun FlashCardItem(navController: NavController, flashCard: FlashCard, flashCardViewModel: FlashCardViewModel) {
    var openDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("destination") }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(flashCard.question)
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(0.dp, (-10).dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navController.navigate("EditFlashCard/${flashCard.id}")
                    },
                    modifier = Modifier
                        .offset((-10).dp)
                ) {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "Edit Card"
                    )
                }
                Button(
                    onClick = {
                        val searchUrl = "https://www.google.com/search?q=${Uri.encode(flashCard.question)}"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(searchUrl)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = "Search Question"
                    )
                }
                Button(
                    onClick = {
                        openDialog = true
                    },
                    modifier = Modifier
                        .offset(10.dp)
                ) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Delete Card"
                    )
                }

                if (openDialog) {
                    AlertDialog(
                        onDismissRequest = { openDialog = false },
                        title = { Text("Are you sure?") },
                        text = { Text("Click confirm to delete this question") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog = false
                                    flashCardViewModel.deleteFlashCard(flashCard.id)
                                }
                            ) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openDialog = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}



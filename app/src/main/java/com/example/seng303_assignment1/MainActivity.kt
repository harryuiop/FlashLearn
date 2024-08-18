package com.example.seng303_assignment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.example.seng303_assignment1.screens.EditFlashCard
import com.example.seng303_assignment1.screens.EndGame
import com.example.seng303_assignment1.screens.FlashCardList
import com.example.seng303_assignment1.screens.NewFlashCard
import com.example.seng303_assignment1.screens.Play
import com.example.seng303_assignment1.ui.theme.Seng303assignment1Theme
import com.example.seng303_assignment1.viewModels.EditFlashCardViewModel
import com.example.seng303_assignment1.viewModels.FlashCardViewModel
import com.example.seng303_assignment1.viewModels.NewFlashCardViewModel
import com.example.seng303_assignment1.viewModels.PlayViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {

    private val flashCardViewModel: FlashCardViewModel by koinViewModel()
    private val newFlashCardViewModel: NewFlashCardViewModel by koinViewModel()
    private val editFlashCardViewModel: EditFlashCardViewModel by koinViewModel()
    private val playViewModel: PlayViewModel by koinViewModel()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flashCardViewModel.loadDefaultFlashCardIfNoneExist()
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }
            Seng303assignment1Theme(darkTheme = isDarkTheme){
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Flash Cards App") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                Home(navController = navController, isDarkTheme, onToggleTheme = {
                                    isDarkTheme = !isDarkTheme
                                })
                            }
                            composable("NewFlashCard") {
                                NewFlashCard(
                                    navController = navController,
                                    flashCardViewModel = flashCardViewModel,
                                    newFlashCardViewModel = newFlashCardViewModel
                                )
                                newFlashCardViewModel.updateQuestion("")
                                newFlashCardViewModel.refreshAnswerOptions()
                            }
                            composable("EditFlashCard/{flashCardId}", arguments = listOf(navArgument("flashCardId") {
                                type = NavType.StringType
                            })
                            ){ backStackEntry ->
                                val flashCardId = backStackEntry.arguments?.getString("flashCardId")
                                flashCardId?.let { flashCardIdParam: String ->
                                    EditFlashCard(
                                        navController = navController,
                                        flashCardIdParam = flashCardIdParam,
                                        flashCardViewModel = flashCardViewModel,
                                        editFlashCardViewModel = editFlashCardViewModel
                                    )
                                }
                            }
                            composable("ViewFlashCards") {
                                FlashCardList(navController = navController, flashCardViewModel = flashCardViewModel)
                            }
                            composable("Play") {
                                Play(
                                    navController = navController,
                                    flashCardViewModel = flashCardViewModel,
                                    playViewModel = playViewModel
                                )
                            }
                            composable("EndGame") {
                                EndGame(
                                    playViewModel = playViewModel,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Home(navController: NavController, isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("ViewFlashCards") }) {
            Text("View Flash Cards")
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Icon(
                Icons.Rounded.Home,
                contentDescription = "View Flash Cards"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("NewFlashCard") }) {
            Text("Create Flash Cards")
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Icon(
                Icons.Rounded.Create,
                contentDescription = "Create Flash Cards"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("Play") }) {
            Text("Play Flash Cards")
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = "Play Flash Cards"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onToggleTheme() }) {
            Text(if (isDarkTheme) "Light Mode" else "Dark Mode")
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Icon(
                Icons.Rounded.Build,
                contentDescription = "Toggle Theme"
            )
        }
    }
}


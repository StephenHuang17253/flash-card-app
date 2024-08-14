package nz.ac.canterbury.seng303.flashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nz.ac.canterbury.seng303.flashcardapp.screens.CreateFlashCard
import nz.ac.canterbury.seng303.flashcardapp.screens.EditFlashCardScreen
import nz.ac.canterbury.seng303.flashcardapp.screens.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.screens.FlashCardList
import nz.ac.canterbury.seng303.flashcardapp.screens.PlayFlashCards
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.Lab2Theme
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.CreateFlashCardViewModel
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    private val flashCardViewModel: FlashCardViewModel by koinViewModel()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flashCardViewModel.loadDefaultCardsIfNoneExist()
        setContent {
            Lab2Theme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        // Add your AppBar content here
                        TopAppBar(
                            title = { Text("SENG303 Assignment 1") },
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
                        val createFlashCardViewModel: CreateFlashCardViewModel = viewModel()
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                Home(navController = navController)
                            }
                            composable(
                                "FlashCard/{cardId}",
                                arguments = listOf(navArgument("cardId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val cardId = backStackEntry.arguments?.getString("cardId")
                                cardId?.let { cardIdParam: String -> FlashCard(cardIdParam, flashCardViewModel) }
                            }
                            composable("FlashCardList") {
                                FlashCardList(navController, flashCardViewModel)
                            }
                            composable("CreateFlashCard") {
                                CreateFlashCard(
                                    navController = navController,
                                    question = createFlashCardViewModel.question,
                                    onQuestionChange = { newTitle -> createFlashCardViewModel.updateQuestion(newTitle)},
                                    answers = createFlashCardViewModel.answers,
                                    onAnswerChange = {index, newAnswer -> createFlashCardViewModel.updateAnswer(index, newAnswer)},
                                    correctAnswerIndex = createFlashCardViewModel.correctAnswerIndex,
                                    onCorrectAnswerChange = {newCorrectAnswer -> createFlashCardViewModel.updateCorrectAnswer(newCorrectAnswer)},
                                    createFlashCardFn = { question, answers, correctAnswerIndex -> flashCardViewModel.createFlashCard(question, answers, correctAnswerIndex)}
                                    )
                            }
                            composable(
                                "EditFlashCard/{cardId}",
                                arguments = listOf(navArgument("cardId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val cardId = backStackEntry.arguments?.getString("cardId")
                                cardId?.let { EditFlashCardScreen(
                                    navController = navController,
                                    flashCardViewModel,
                                    cardId = cardId.toInt(),
                                ) }
                            }
                            composable("PlayFlashCard") {
                                PlayFlashCards(flashCardViewModel = flashCardViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to my Flash Card App")
        Button(onClick = { navController.navigate("FlashCardList") }) {
            Text("View Flash Cards")
        }
        Button(onClick = { navController.navigate("CreateFlashCard") }) {
            Text("Create Flash Card")
        }
        Button(onClick = { navController.navigate("PlayFlashCard") }) {
            Text("Play Flash Cards")
        }
    }
}

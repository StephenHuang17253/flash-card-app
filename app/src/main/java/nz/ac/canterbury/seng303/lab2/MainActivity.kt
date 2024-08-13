package nz.ac.canterbury.seng303.lab2

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
import androidx.compose.material.icons.filled.ArrowBack
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
import nz.ac.canterbury.seng303.lab2.screens.CreateNote
import nz.ac.canterbury.seng303.lab2.screens.EditNoteScreen
import nz.ac.canterbury.seng303.lab2.screens.NoteCard
import nz.ac.canterbury.seng303.lab2.screens.NoteGrid
import nz.ac.canterbury.seng303.lab2.screens.NoteList
import nz.ac.canterbury.seng303.lab2.ui.theme.Lab2Theme
import nz.ac.canterbury.seng303.lab2.viewmodels.CreateNoteViewModel
import nz.ac.canterbury.seng303.lab2.viewmodels.NoteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    private val noteViewModel: NoteViewModel by koinViewModel()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel.loadDefaultNotesIfNoneExist()
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
                        val createNoteViewModel: CreateNoteViewModel = viewModel()
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                Home(navController = navController)
                            }
                            composable(
                                "NoteCard/{noteId}",
                                arguments = listOf(navArgument("noteId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val noteId = backStackEntry.arguments?.getString("noteId")
                                noteId?.let { noteIdParam: String -> NoteCard(noteIdParam, noteViewModel) }
                            }
                            composable("NoteList") {
                                NoteList(navController, noteViewModel)
                            }
                            composable("NoteGrid") {
                                NoteGrid(navController)
                            }
                            composable("CreateNote") {
                                CreateNote(
                                    navController = navController,
                                    title = createNoteViewModel.title,
                                    onTitleChange = {newTitle -> createNoteViewModel.updateTitle(newTitle)},
                                    answers = createNoteViewModel.answers,
                                    onAnswerChange = {index, newAnswer -> createNoteViewModel.updateAnswer(index, newAnswer)},
                                    correctAnswerIndex = createNoteViewModel.correctAnswerIndex,
                                    onCorrectAnswerChange = {newCorrectAnswer -> createNoteViewModel.updateCorrectAnswer(newCorrectAnswer)},
                                    createNoteFn = {title, answers, correctAnswerIndex -> noteViewModel.createNote(title, answers, correctAnswerIndex)}
                                    )
                            }
                            composable(
                                "EditNote/{noteId}",
                                arguments = listOf(navArgument("noteId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val noteId = backStackEntry.arguments?.getString("noteId")
                                noteId?.let { EditNoteScreen(
                                    navController = navController,
                                    noteViewModel,
                                    noteId = noteId.toInt(),
                                ) }
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
        Button(onClick = { navController.navigate("NoteList") }) {
            Text("View Flash Cards")
        }
        Button(onClick = { navController.navigate("CreateNote") }) {
            Text("Create Flash Card")
        }
//        Button(onClick = { navController.navigate("NoteGrid") }) {
//            Text("Note Grid")
//        }
    }
}

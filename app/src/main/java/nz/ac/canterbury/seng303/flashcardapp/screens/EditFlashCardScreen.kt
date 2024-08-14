package nz.ac.canterbury.seng303.flashcardapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFlashCardScreen(navController: NavController,
                        flashCardViewModel: FlashCardViewModel = viewModel(),
                        cardId: Int
                    ) {
    val card by flashCardViewModel.selectedFlashCard.collectAsState()
    val (question, setQuestion) = rememberSaveable { mutableStateOf("") }
    val (answers, setAnswers) = rememberSaveable {  mutableStateOf(mutableListOf("")) }
    val (correctAnswerIndex, setCorrectAnswerIndex) = rememberSaveable { mutableIntStateOf(-1) }

    // Load card details if card is not already loaded
    LaunchedEffect(cardId) {
        flashCardViewModel.getCardById(cardId)
    }

    // Update question and answers when the card changes
    LaunchedEffect(card) {
        card?.let {
            setQuestion(it.question)
            setAnswers(it.answers.toMutableList())
            setCorrectAnswerIndex(it.correctAnswer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = question,
            onValueChange = { setQuestion(it) },
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Answer inputs
        answers.forEachIndexed { index, answer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = answer,
                    onValueChange = { newAnswer ->
                        setAnswers(answers.toMutableList().apply { this[index] = newAnswer })
                    },
                    label = { Text("Answer ${index + 1}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Checkbox(
                    checked = correctAnswerIndex == index,
                    onCheckedChange = { isChecked ->
                        if (isChecked) setCorrectAnswerIndex(index)
                    }
                )
                Text(text = "Correct")
            }
        }

        // Button to add more answer fields
        Button(
            onClick = {
                if (answers.size < 5) {
                    setAnswers(answers.toMutableList().apply { add("") })
                }
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            Text(text = "+")
        }

        Button(
            onClick = {
                card?.let {
                    flashCardViewModel.editFlashCard(
                        it.id,
                        question,
                        answers,
                        correctAnswerIndex
                    )
                }
                navController.navigate("NoteList")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Note")
        }
    }
}



package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.EditFlashCardViewModel
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFlashCardScreen(navController: NavController,
                        flashCardViewModel: FlashCardViewModel = viewModel(),
                        cardId: Int,
                        editFlashCardViewModel: EditFlashCardViewModel = viewModel()
    ) {
    val context = LocalContext.current
    val selectedCardState by flashCardViewModel.selectedFlashCard.collectAsState(null)
    val card: FlashCard? = selectedCardState

    LaunchedEffect(cardId) {
        flashCardViewModel.getCardById(cardId)
    }

    LaunchedEffect(selectedCardState) {
        selectedCardState?.let { card ->
            if (editFlashCardViewModel.question.isEmpty()) {
                editFlashCardViewModel.setDefaultValues(card)
            }
        }
    }

    ElevatedCard(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = "Edit Flash Card",
                    style = MaterialTheme.typography.headlineLarge
                )
            }


            OutlinedTextField(
                value = editFlashCardViewModel.question,
                onValueChange = {
                    editFlashCardViewModel.updateQuestion(it)
                },
                label = { Text("Question") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .padding(bottom = 8.dp),
                maxLines = 4
            )

            // Answer inputs
            editFlashCardViewModel.answers.forEachIndexed { index, answer ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = answer.text,
                        onValueChange = { newAnswer ->
                            editFlashCardViewModel.updateAnswer(answer.id, newAnswer)
                        },
                        label = { Text("Answer ${index + 1}") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    Checkbox(
                        checked = editFlashCardViewModel.correctAnswerIndex == index,
                        onCheckedChange = { isChecked ->
                            if (isChecked) editFlashCardViewModel.updateCorrectAnswerIndex(index)
                        }
                    )
                    Text(text = "Correct")
                }
            }

            // Button to add more answer fields
            Button(
                onClick = {
                    editFlashCardViewModel.addAnswer()
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "+")
            }

            Button(
                onClick = {
                    flashCardViewModel.editFlashCard(
                        cardId = cardId,
                        FlashCard(id = cardId,
                            question = editFlashCardViewModel.question,
                            answers = editFlashCardViewModel.answers,
                            correctAnswer = editFlashCardViewModel.correctAnswerIndex,
                            timestamp = System.currentTimeMillis()))

                    Toast.makeText(context, "Flash card edited.", Toast.LENGTH_SHORT).show()
                    navController.navigate("FlashCardList")

                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Update Card")
            }


        }
    }
    }



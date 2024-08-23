package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    val scrollState = rememberScrollState()

    ElevatedCard(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
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

                    // Checkbox to mark correct answer
                    Checkbox(
                        checked = editFlashCardViewModel.correctAnswerId == answer.id,
                        onCheckedChange = { isChecked ->
                            if (isChecked) editFlashCardViewModel.updateCorrectAnswerId(answer.id)
                        }
                    )
//                    Text(text = "Correct")

                    // X Button for removing an answer
                    IconButton(onClick = {
                        editFlashCardViewModel.removeAnswer(answer.id)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete, // Use the Close icon
                            contentDescription = "Remove Answer",
                        )
                    }


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
                    // Input validation
                    if (editFlashCardViewModel.question.isBlank()) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("A flash card can't have an empty question.")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                        return@Button
                    }

                    if (editFlashCardViewModel.answers.size < 2) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("A flash card must have at least 2 answers.")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                        return@Button
                    }

                    if (editFlashCardViewModel.answers.any { it.text.trim().isEmpty() }) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("An answer cannot be blank.")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                        return@Button
                    }

                    if (editFlashCardViewModel.correctAnswerId !in editFlashCardViewModel.answers.map {it.id}) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("A flash card must have 1 correct answer.")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                        return@Button
                    }


                    flashCardViewModel.editFlashCard(
                        cardId = cardId,
                        FlashCard(id = cardId,
                            question = editFlashCardViewModel.question,
                            answers = editFlashCardViewModel.answers,
                            correctAnswer = editFlashCardViewModel.correctAnswerId,
                            timestamp = System.currentTimeMillis()))

                    Toast.makeText(context, "Flash card edited.", Toast.LENGTH_SHORT).show()

//                    val builder = AlertDialog.Builder(context)
//                    builder.setMessage("Where do you want to go")
//                        .setCancelable(false)
//                        .setPositiveButton("Home") { dialog, id ->
//                            navController.navigate("Home")
//                            dialog.dismiss()
//                        }
//                        .setNegativeButton("View Flash Cards") { dialog, id ->
//                            navController.navigate("FlashCardList")
//                            dialog.dismiss()
//                        }
//                    val alert = builder.create()
//                    alert.show()

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



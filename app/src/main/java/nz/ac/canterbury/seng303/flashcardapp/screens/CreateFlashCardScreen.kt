package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCardAnswer
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.CreateFlashCardViewModel
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.EditFlashCardViewModel
import java.util.UUID

@Composable
fun CreateFlashCard(navController: NavController,
                    createFlashCardFn: (String, List<FlashCardAnswer>, Int) -> Unit,
                    createFlashCardViewModel: CreateFlashCardViewModel = viewModel()
               ) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val bookPaperColor = Color(0xFFFFF8E1) // Light beige color

    ElevatedCard(
        modifier = Modifier.padding(16.dp),
//        colors = CardDefaults.cardColors(containerColor = bookPaperColor)

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            {
                Text(
                    text = "Add Flash Card",
                    style = MaterialTheme.typography.headlineLarge,

                    )

            }
            HorizontalDivider()


            OutlinedTextField(
                value = createFlashCardViewModel.question,
                onValueChange = { createFlashCardViewModel.updateQuestion(it) },
                label = { Text("Question") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .padding(bottom = 8.dp),
                maxLines = 4
            )

            // Answer inputs
            createFlashCardViewModel.answers.forEachIndexed { index, answer ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)

                ) {
                    OutlinedTextField(
                        value = answer.text,
                        onValueChange = { newAnswerText ->
                            createFlashCardViewModel.updateAnswer(answer.id, newAnswerText)
                        },
                        label = { Text("Answer ${index + 1}") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    // Checkbox for correct answer
                    Checkbox(
                        checked = createFlashCardViewModel.correctAnswerId == answer.id,
                        onCheckedChange = { isChecked ->
                            if (isChecked) createFlashCardViewModel.updateCorrectAnswer(answer.id)
                        }
                    )
//                    Text(text = "Correct")

                    // X Button for removing an answer
                    IconButton(onClick = {
                        createFlashCardViewModel.removeAnswer(answer.id)
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
                    if (createFlashCardViewModel.answers.size < 5) {
                        createFlashCardViewModel.addAnswer()
                    }
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "+")
            }

            // Save button
            Button(
                onClick = {
                    // Input validation
                    if (createFlashCardViewModel.question.isBlank()) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("A flash card can't have an empty question.")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                        return@Button
                    }

                    if (createFlashCardViewModel.answers.size < 2) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("A flash card must have at least 2 answers.")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                        return@Button
                    }

                    if (createFlashCardViewModel.answers.any { it.text.trim().isEmpty() }) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("An answer cannot be blank.")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                        return@Button
                    }

                    if (createFlashCardViewModel.correctAnswerId !in createFlashCardViewModel.answers.map {it.id}) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("A flash card must have 1 correct answer.")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                        return@Button
                    }

                    createFlashCardFn(createFlashCardViewModel.question, createFlashCardViewModel.answers, createFlashCardViewModel.correctAnswerId)
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Created flash card!")
                        .setCancelable(false)
                        .setPositiveButton("Ok") { dialog, id -> /* Run some code on click */
                            navController.navigate("FlashCardList")
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            // Dismiss the dialog
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()

                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Save and return")
            }
        }
    }


}


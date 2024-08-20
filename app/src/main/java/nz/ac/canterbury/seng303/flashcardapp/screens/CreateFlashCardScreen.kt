package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCardAnswer
import java.util.UUID

@Composable
fun CreateFlashCard(navController: NavController,
                    question: String,
                    onQuestionChange: (String) -> Unit,
                    answers: List<FlashCardAnswer>,
                    onAnswerChange: (Int, String) -> Unit,
                    correctAnswerId: Int,
                    onCorrectAnswerChange: (Int) -> Unit,
                    createFlashCardFn: (String, List<FlashCardAnswer>, Int) -> Unit
               ) {
    val context = LocalContext.current
    var answerFields by remember { mutableStateOf(answers.toMutableList()) }
    var nextId by remember { mutableStateOf(answerFields.size) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
            value = question,
            onValueChange = { onQuestionChange(it) },
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .padding(bottom = 8.dp),
            maxLines = 4
        )

        // Answer inputs
        answerFields.forEachIndexed { index, answer ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)

            ) {
                OutlinedTextField(
                    value = answer.text,
                    onValueChange = { newAnswerText ->
                        onAnswerChange(answer.id, newAnswerText)
                        answerFields = answerFields.map { if (it.id == answer.id) it.copy(text = newAnswerText) else it }.toMutableList()
                    },
                    label = { Text("Answer ${index + 1}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                // Checkbox for correct answer
                Checkbox(
                    checked = correctAnswerId == answer.id,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            onCorrectAnswerChange(answer.id)
                        }
                    }
                )
                Text(text = "Correct")
            }
        }

        // Button to add more answer fields
        Button(
            onClick = {
                if (answerFields.size < 5) {
                    val newId = nextId // Use the counter value for the new ID
                    nextId++ // Increment the counter for the next ID
                    answerFields = answerFields.toMutableList().apply { add(FlashCardAnswer(newId, "")) }
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
                if (question.isBlank()) {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("A flash card can't have an empty question.")
                        .setCancelable(false)
                        .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                    return@Button
                }

                if (answerFields.size < 2) {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("A flash card must have at least 2 answers.")
                        .setCancelable(false)
                        .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                    return@Button
                }

                if (answerFields.any { it.text.trim().isEmpty() }) {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("An answer cannot be blank.")
                        .setCancelable(false)
                        .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                    return@Button
                }

                if (correctAnswerId !in answerFields.map {it.id}) {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("A flash card must have 1 correct answer.")
                        .setCancelable(false)
                        .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                    return@Button
                }

                createFlashCardFn(question, answerFields, correctAnswerId)
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Created flash card!")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { dialog, id -> /* Run some code on click */
                        onQuestionChange("")
                        answerFields.forEach{ answer ->
                            onAnswerChange(answer.id, "")
                        }
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


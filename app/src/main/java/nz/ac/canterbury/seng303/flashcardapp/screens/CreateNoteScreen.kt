package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CreateNote(navController: NavController,
               title: String,
               onTitleChange: (String) -> Unit,
               answers: List<String>,
               onAnswerChange: (Int, String) -> Unit,
               correctAnswerIndex: Int,
               onCorrectAnswerChange: (Int) -> Unit,
               createNoteFn: (String, List<String>, Int) -> Unit
               ) {
    val context = LocalContext.current
    var answerFields by remember { mutableStateOf(answers.toMutableList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {onTitleChange(it)},
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .padding(bottom = 8.dp),
            maxLines = 5
        )

        // Answer inputs
        answerFields.forEachIndexed { index, answer ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = answer,
                    onValueChange = { newAnswer ->
                        onAnswerChange(index, newAnswer)
                        answerFields = answerFields.toMutableList().apply { this[index] = newAnswer }
                    },
                    label = { Text("Answer ${index + 1}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                // Checkbox for correct answer
                Checkbox(
                    checked = correctAnswerIndex == index,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            onCorrectAnswerChange(index)
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
                    answerFields = answerFields.toMutableList().apply { add("") }
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
                createNoteFn(title, answerFields, correctAnswerIndex)
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Created flash card!")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { dialog, id -> /* Run some code on click */
                        onTitleChange("")
                        answerFields.forEachIndexed { index, _ ->
                            onAnswerChange(index, "")
                        }
                        navController.navigate("noteList")
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
            Text(text = "Save")
        }
    }
}


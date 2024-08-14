package nz.ac.canterbury.seng303.flashcardapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun PlayFlashCards(flashCardViewModel: FlashCardViewModel) {
    flashCardViewModel.getCards()
    val flashCards by flashCardViewModel.flashCards.collectAsState(emptyList())

    val (currentIndex, setCurrentIndex) = remember { mutableStateOf(0) }
    val (selectedAnswer, setSelectedAnswer) = remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    if (flashCards.isNotEmpty()) {
        val flashCard = flashCards[currentIndex]



        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // Display progress counter
            Text(
                text = "Question ${currentIndex + 1} of ${flashCards.size}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Display question
            Text(
                text = flashCard.question,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            // Display answers
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                flashCard.answers.forEachIndexed { index, answer ->
                    Button(
                        onClick = { setSelectedAnswer(answer) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "${index + 1}. $answer")
                    }
                }
            }

            // Display correct answer if selected
            selectedAnswer?.let { answer ->
                Text(
                    text = if (answer == flashCard.answers[flashCard.correctAnswer]) {
                        "Correct!"
                    } else {
                        "Incorrect. The correct answer is: ${flashCard.answers[flashCard.correctAnswer]}"
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Show toast message if an answer has been selected
            LaunchedEffect(selectedAnswer) {
                selectedAnswer?.let {
                    if (it == flashCard.answers[flashCard.correctAnswer]) {
                        Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Incorrect. The correct answer is: ${flashCard.answers[flashCard.correctAnswer]}", Toast.LENGTH_SHORT).show()
                    }
                    setSelectedAnswer(null)  // Reset selected answer
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (currentIndex > 0) setCurrentIndex(currentIndex - 1) },
                    enabled = currentIndex > 0
                ) {
                    Text(text = "Previous")
                }
                Button(
                    onClick = {
                        if (currentIndex < flashCards.size - 1) setCurrentIndex(currentIndex + 1)
                    },
                    enabled = currentIndex < flashCards.size - 1
                ) {
                    Text(text = "Next")
                }
            }
        }
    } else {
        Text(
            text = "No flashcards available.",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

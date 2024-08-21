package nz.ac.canterbury.seng303.flashcardapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCardAnswer
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun PlayFlashCards(flashCardViewModel: FlashCardViewModel) {
    flashCardViewModel.getCards()
    val flashCards by flashCardViewModel.flashCards.collectAsState(emptyList())

    val (currentIndex, setCurrentIndex) = remember { mutableStateOf(0) }
    val (selectedAnswer, setSelectedAnswer) = remember { mutableStateOf<FlashCardAnswer?>(null) }
    val (answerSubmitted, setAnswerSubmitted) = remember { mutableStateOf(false) }
    val (answersHistory, setAnswersHistory) = remember { mutableStateOf(emptyList<Pair<FlashCardAnswer?, Boolean>>()) }
    val context = LocalContext.current

    val isSummaryVisible = flashCards.isNotEmpty() && answersHistory.size == flashCards.size


    ElevatedCard(
//        colors = CardColors(Color.hsl(210F, 100F, 80F), Color.Black, Color.Black, Color.Black),
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column {

            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = "Playing Flash Cards",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            if (flashCards.isNotEmpty()) {
                if (!isSummaryVisible) {
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

                        // Display answers with checkboxes
                        Column(modifier = Modifier.padding(bottom = 16.dp)) {
                            flashCard.answers.forEachIndexed { index, answer ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Checkbox for each answer
                                    RadioButton(
                                        selected = (answer == selectedAnswer),
                                        onClick = { setSelectedAnswer(answer) }
                                    )
                                    Text(
                                        text = answer.text,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }

                        // Show toast message and navigate to the next flash card
                        LaunchedEffect(selectedAnswer, answerSubmitted) {
                            if (answerSubmitted) {
                                selectedAnswer?.let {
                                    if (it.id == flashCard.correctAnswer) {
                                        Toast.makeText(context, "Correct answer!", Toast.LENGTH_SHORT).show()
                                        setAnswersHistory(answersHistory + (selectedAnswer to true))
                                    } else {
                                        val correctAnswer = flashCard.answers.find { it.id == flashCard.correctAnswer }
                                        Toast.makeText(context, "Incorrect. The correct answer is: ${correctAnswer?.text}", Toast.LENGTH_SHORT).show()
                                        setAnswersHistory(answersHistory + (selectedAnswer to false))
                                    }
                                }
                                // Move to the next flash card
                                if (currentIndex < flashCards.size - 1) {
                                    setCurrentIndex(currentIndex + 1)

                                }
                                setSelectedAnswer(null)  // Reset selected answer
                                setAnswerSubmitted(false) // Reset submission state
                            }
                        }

                        // Navigation buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    if (selectedAnswer != null) {
                                        setAnswerSubmitted(true)
                                    } else {
                                        Toast.makeText(context, "Select an answer to submit.", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                enabled = selectedAnswer != null
                            ) {
                                Text(text = "Submit")
                            }
                        }
                    }
                }
                if (isSummaryVisible) {
                    val correctAnswers = answersHistory.count { it.second }
                    val totalQuestions = answersHistory.size
                    if (totalQuestions == flashCards.size) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),

                            ) {
                            Text(
                                text = "Summary",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Text(
                                text = "You answered $correctAnswers out of $totalQuestions questions correctly!",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            HorizontalDivider()
                            // Display detailed results with icons
                            answersHistory.forEachIndexed { index, (answer, isCorrect) ->
                                val question = flashCards.getOrNull(index)?.question ?: "Unknown Question"

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Q${index + 1}. $question",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                                        contentDescription = if (isCorrect) "Correct" else "Incorrect",
                                        tint = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                HorizontalDivider()
                            }

                        }
                    }
                }

            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (flashCards.isEmpty()) {
                        Text(
                            text = "No flash cards to play.",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

            }
        }


    }


}

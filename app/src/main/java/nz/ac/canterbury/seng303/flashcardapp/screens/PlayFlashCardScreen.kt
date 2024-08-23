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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.PlayFlashCardViewModel

@Composable
fun PlayFlashCards(
    flashCardViewModel: FlashCardViewModel,
    playFlashCardViewModel: PlayFlashCardViewModel = viewModel()
) {
    flashCardViewModel.getCards()
    val flashCards by flashCardViewModel.flashCards.collectAsState(emptyList())
    val shuffledFlashCards by playFlashCardViewModel.flashCards.collectAsState()
    val currentIndex by playFlashCardViewModel.currentIndex.collectAsState()
    val selectedAnswer by playFlashCardViewModel.selectedAnswer.collectAsState()
    val answerSubmitted by playFlashCardViewModel.answerSubmitted.collectAsState()
    val answersHistory by playFlashCardViewModel.answersHistory.collectAsState()
    val context = LocalContext.current

    if (flashCards.isNotEmpty() && shuffledFlashCards.isEmpty()) {
        playFlashCardViewModel.setFlashCards(flashCards)
    }

    val isSummaryVisible = shuffledFlashCards.isNotEmpty() && answersHistory.size == shuffledFlashCards.size
    val scrollState = rememberScrollState()
    val bookPaperColor = Color(0xFFFFF8E1)

    ElevatedCard(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Playing Flash Cards",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            if (shuffledFlashCards.isNotEmpty()) {
                if (!isSummaryVisible) {
                    val flashCard = shuffledFlashCards[currentIndex]

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1} of ${shuffledFlashCards.size}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

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

                        Column(modifier = Modifier.padding(bottom = 16.dp)) {
                            flashCard.answers.forEachIndexed { index, answer ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (answer.id == selectedAnswer),
                                        onClick = { playFlashCardViewModel.setSelectedAnswer(answer.id) }
                                    )
                                    Text(
                                        text = answer.text,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }

                        LaunchedEffect(selectedAnswer, answerSubmitted) {
                            if (answerSubmitted) {
                                playFlashCardViewModel.submitAnswer(context)
                            }
                        }

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
                                        playFlashCardViewModel.setAnswerSubmitted(true)
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
                            answersHistory.forEachIndexed { index, (answer, isCorrect) ->
                                val question = shuffledFlashCards.getOrNull(index)?.question ?: "Unknown Question"
                                ElevatedCard(
                                    modifier = Modifier.padding(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Q${index + 1}. $question",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(16.dp)
                                        )
                                        Icon(
                                            imageVector = if (isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                                            contentDescription = if (isCorrect) "Correct" else "Incorrect",
                                            tint = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
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

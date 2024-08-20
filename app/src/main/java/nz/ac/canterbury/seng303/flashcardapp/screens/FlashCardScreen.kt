package nz.ac.canterbury.seng303.flashcardapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.util.convertTimestampToReadableTime
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun FlashCard(cardId: String, flashCardViewModel: FlashCardViewModel) {
    flashCardViewModel.getCardById(cardId = cardId.toIntOrNull())
    val selectedCardState by flashCardViewModel.selectedFlashCard.collectAsState(null)
    val flashCard: FlashCard? = selectedCardState

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (flashCard != null) {
            Text(text = "${flashCard.question}", style = MaterialTheme.typography.headlineMedium)
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
            Text(
                text = "Answers:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Column(modifier = Modifier.padding(start = 4.dp)) {
                flashCard.answers.forEachIndexed { index, answer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• ${answer.text}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            val correctAnswer = flashCard.answers.find { it.id == flashCard.correctAnswer }
            if (correctAnswer != null) {
                Text(
                    text = "\nCorrect Answer: ${correctAnswer.text}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            Row {
                Text(
                    text = "\nLast modified on " + convertTimestampToReadableTime(flashCard.timestamp),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
        } else {
            Text(
                text = "Could not find card: $cardId",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
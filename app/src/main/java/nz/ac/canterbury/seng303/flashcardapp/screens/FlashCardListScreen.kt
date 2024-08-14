package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.util.convertTimestampToReadableTime
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun FlashCardList(navController: NavController, flashCardViewModel: FlashCardViewModel) {
    flashCardViewModel.getCards()
    val flashCards: List<FlashCard> by flashCardViewModel.flashCards.collectAsState(emptyList())

    if (flashCards.isEmpty()) {
        Text(
            text = "There are no cards created.",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }

    LazyColumn {
        items(flashCards) { flashCard ->
            FlashCardItem(navController = navController, flashCard = flashCard, flashCardViewModel)
            Divider() // Add a divider between items
        }
    }
}

@Composable
fun FlashCardItem(navController: NavController, flashCard: FlashCard, flashCardViewModel: FlashCardViewModel) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("FlashCard/${flashCard.id}") },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Display title and timestamp
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
        ) {
            Text(
                text = flashCard.question,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = convertTimestampToReadableTime(flashCard.timestamp),
                style = MaterialTheme.typography.bodyLarge
            )
            // Display edit and delete buttons (icons)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()


            ) {
                Button(onClick = {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Google this question?\n\n \"${flashCard.question}\"?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { dialog, id ->
                            val encodedQuestion = Uri.encode(flashCard.question)
                            val searchUrl = "https://www.google.com/search?q=$encodedQuestion"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No application to handle this action.", Toast.LENGTH_SHORT).show()
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            // Dismiss the dialog
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }

                Button(onClick = {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Edit Flash Card?\n\n \"${flashCard.question}\"")
                        .setCancelable(false)
                        .setPositiveButton("Edit") { dialog, id ->
                            navController.navigate("EditFlashCard/${flashCard.id}")
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            // Dismiss the dialog
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()



                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.White
                    )
                }
                Button(onClick = {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Are you sure you want to delete this flash card?\n\n \"${flashCard.question}\"")
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color='#FF0000'>Delete</font>")) { dialog, id ->
                            flashCardViewModel.deleteFlashCard(flashCard.id)
                            Toast.makeText(context, "Flash card deleted.", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            // Dismiss the dialog
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }

            }

        }
    }
}
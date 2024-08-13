package nz.ac.canterbury.seng303.lab2.screens

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import nz.ac.canterbury.seng303.lab2.models.Note
import nz.ac.canterbury.seng303.lab2.util.convertTimestampToReadableTime
import nz.ac.canterbury.seng303.lab2.viewmodels.NoteViewModel

@Composable
fun NoteList(navController: NavController, noteViewModel: NoteViewModel) {
    noteViewModel.getNotes()
    val notes: List<Note> by noteViewModel.notes.collectAsState(emptyList())
    LazyColumn {
        items(notes) { note ->
            NoteItem(navController = navController, note = note, noteViewModel)
            Divider() // Add a divider between items
        }
    }
}

@Composable
fun NoteItem(navController: NavController, note: Note, noteViewModel: NoteViewModel) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("NoteCard/${note.id}") },
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
                text = note.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = convertTimestampToReadableTime(note.timestamp),
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
                    builder.setMessage("Edit note: \"${note.title}\"?")
                        .setCancelable(false)
                        .setPositiveButton("Edit") { dialog, id ->
                            navController.navigate("EditNote/${note.id}")
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
                    builder.setMessage("Delete note: \"${note.title}\"?")
                        .setCancelable(false)
                        .setPositiveButton("Delete") { dialog, id ->
                            noteViewModel.deleteNote(note.id)
                            Toast.makeText(context, "Note deleted.", Toast.LENGTH_SHORT).show()
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
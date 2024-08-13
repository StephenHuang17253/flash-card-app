package nz.ac.canterbury.seng303.flashcardapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen( navController: NavController,
                    noteViewModel: NoteViewModel = viewModel(),
                    noteId: Int
                    ) {
    val note by noteViewModel.selectedNote.collectAsState()
    val (title, setTitle) = rememberSaveable { mutableStateOf("") }
    val (content, setContent) = rememberSaveable { mutableStateOf("") }
    val (archived, setArchived) = rememberSaveable { mutableStateOf(false) }

    // Load note details if note is not already loaded
    LaunchedEffect(noteId) {
        noteViewModel.getNoteById(noteId)
    }

    // Update title and content when the note changes
    LaunchedEffect(note) {
        note?.let {
            setTitle(it.title)
            setContent(it.content)
            setArchived(it.isArchived)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { setTitle(it) },
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = content,
            onValueChange = { setContent(it) },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .fillMaxHeight()
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = archived,
                onCheckedChange = { setArchived(it) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Archived")
        }

        Button(
            onClick = {
                note?.let {
                    noteViewModel.editNote(
                        it.id,
                        title,
                        content,
                        archived
                    )
                }
                navController.navigate("NoteList")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Note")
        }
    }
}



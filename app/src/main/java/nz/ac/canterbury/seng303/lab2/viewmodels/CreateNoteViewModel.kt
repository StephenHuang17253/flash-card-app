package nz.ac.canterbury.seng303.lab2.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class CreateNoteViewModel: ViewModel() {
    var title by mutableStateOf("")
        private set

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    var answers by mutableStateOf(mutableListOf(""))
        private set

    fun updateAnswer(index: Int, newAnswer: String) {
        if (index in answers.indices) {
            answers = answers.toMutableList().apply {
                this[index] = newAnswer
            }
        }
    }

    var correctAnswerIndex by mutableIntStateOf(-1)
        private set

    fun updateCorrectAnswer(newIndex: Int) {
        correctAnswerIndex = newIndex
    }

}
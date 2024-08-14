package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreateFlashCardViewModel: ViewModel() {
    var question by mutableStateOf("")
        private set

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
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
package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard

class EditFlashCardViewModel : ViewModel() {
    var question by mutableStateOf("")
        private set

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    var answers by mutableStateOf(mutableListOf<String>())
        private set

    fun updateAnswer(index: Int, newAnswer: String) {
        answers = answers.toMutableList().apply { this[index] = newAnswer }
    }

    fun addAnswer() {
        if (answers.size < 5) {
            answers = answers.toMutableList().apply { add("") }
        }
    }

    var correctAnswerIndex by mutableStateOf(-1)
        private set

    fun updateCorrectAnswerIndex(index: Int) {
        correctAnswerIndex = index
    }

    fun setDefaultValues(card: FlashCard?) {
        card?.let {
            question = it.question
            answers = it.answers.toMutableList()
            correctAnswerIndex = it.correctAnswer
        }
    }
}

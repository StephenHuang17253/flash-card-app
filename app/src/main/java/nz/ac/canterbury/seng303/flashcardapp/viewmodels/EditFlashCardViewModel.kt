package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCardAnswer
import java.util.UUID

class EditFlashCardViewModel : ViewModel() {
    var question by mutableStateOf("")
        private set

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    var answers by mutableStateOf(mutableListOf<FlashCardAnswer>())
        private set

    fun updateAnswer(id: Int, newAnswer: String) {
        answers = answers.map { if (it.id == id) it.copy(text = newAnswer) else it }.toMutableList()
    }

    fun addAnswer() {
        if (answers.size < 5) {
            val newId = UUID.randomUUID().toString().hashCode() // Generate a unique ID
            answers = answers.toMutableList().apply {
                add(FlashCardAnswer(newId, ""))
            }
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

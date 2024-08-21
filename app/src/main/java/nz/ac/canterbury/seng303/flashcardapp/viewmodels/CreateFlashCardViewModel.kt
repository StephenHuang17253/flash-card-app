package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCardAnswer
import java.util.UUID

class CreateFlashCardViewModel: ViewModel() {
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

    fun removeAnswer(id: Int) {
        answers = answers.filter { it.id != id }.toMutableList()
        if (correctAnswerId == id) {
            correctAnswerId = -1
        }
    }

    var correctAnswerId by mutableIntStateOf(-1)
        private set

    fun updateCorrectAnswer(id: Int) {
        correctAnswerId = id
    }

}
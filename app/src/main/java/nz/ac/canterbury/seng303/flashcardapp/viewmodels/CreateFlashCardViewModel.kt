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

    var answers by mutableStateOf(mutableStateListOf<FlashCardAnswer>())
        private set

    fun updateAnswer(id: Int, newAnswer: String) {
        val answer = answers.find { it.id == id }
        answer?.text = newAnswer
        }

    fun addAnswer() {
        if (answers.size < 5) {
            val newId = UUID.randomUUID().toString().hashCode()
            answers.add(FlashCardAnswer(newId, ""))
        }
    }

    var correctAnswerId by mutableIntStateOf(-1)
        private set

    fun updateCorrectAnswer(id: Int) {
        correctAnswerId = id
    }

}
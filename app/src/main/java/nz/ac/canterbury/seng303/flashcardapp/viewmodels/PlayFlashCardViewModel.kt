package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard

class PlayFlashCardViewModel : ViewModel() {
    private val _flashCards = MutableStateFlow<List<FlashCard>>(emptyList())
    val flashCards: StateFlow<List<FlashCard>> = _flashCards

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _selectedAnswer = MutableStateFlow<Int?>(null)
    val selectedAnswer: StateFlow<Int?> = _selectedAnswer

    private val _answerSubmitted = MutableStateFlow(false)
    val answerSubmitted: StateFlow<Boolean> = _answerSubmitted

    private val _answersHistory = MutableStateFlow<List<Pair<Int?, Boolean>>>(emptyList())
    val answersHistory: StateFlow<List<Pair<Int?, Boolean>>> = _answersHistory

    fun setFlashCards(cards: List<FlashCard>) {
        viewModelScope.launch {
            val shuffledFlashCards = cards.map { flashCard ->
                flashCard.copy(answers = flashCard.answers.shuffled())
            }.shuffled()
            _flashCards.emit(shuffledFlashCards)
        }
    }

    fun setCurrentIndex(index: Int) {
        viewModelScope.launch {
            _currentIndex.emit(index)
        }
    }

    fun setSelectedAnswer(answerId: Int?) {
        viewModelScope.launch {
            _selectedAnswer.emit(answerId)
        }
    }

    fun submitAnswer() {
        viewModelScope.launch {
            val flashCard = _flashCards.value[_currentIndex.value]
            val isCorrect = _selectedAnswer.value == flashCard.correctAnswer
            _answersHistory.emit(_answersHistory.value + (_selectedAnswer.value to isCorrect))

            if (_currentIndex.value < _flashCards.value.size - 1) {
                _currentIndex.emit(_currentIndex.value + 1)
            }
            _selectedAnswer.emit(null)  // Reset selected answer
            _answerSubmitted.emit(false) // Reset submission state
        }
    }

    fun setAnswerSubmitted(submitted: Boolean) {
        viewModelScope.launch {
            _answerSubmitted.emit(submitted)
        }
    }
}

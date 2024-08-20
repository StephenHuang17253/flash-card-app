package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.flashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCardAnswer
import kotlin.random.Random

class FlashCardViewModel(private val flashCardStorage: Storage<FlashCard>): ViewModel() {
    private val _flashCards = MutableStateFlow<List<FlashCard>>(emptyList())
    val flashCards: StateFlow<List<FlashCard>> get() = _flashCards
    private val _selectedFlashCard = MutableStateFlow<FlashCard?>(null)
    val selectedFlashCard: StateFlow<FlashCard?> = _selectedFlashCard

    fun getCardById(cardId: Int?) = viewModelScope.launch {
        if (cardId != null) {
            _selectedFlashCard.value = flashCardStorage.get { it.getIdentifier() == cardId }.first()
        } else {
            _selectedFlashCard.value = null
        }
    }

    fun getCards() = viewModelScope.launch {
        flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }.collect{_flashCards.emit(it)}
    }

    fun loadDefaultCardsIfNoneExist() = viewModelScope.launch {
        val currentCards = flashCardStorage.getAll().first()
        if (currentCards.isEmpty()) {
            Log.d("FLASH_CARD_VIEW_MODEL", "Inserting default flash cards...")
            flashCardStorage.insertAll(FlashCard.getCards()).catch { Log.w("FLASH_CARD_VIEW_MODEL", "Could not insert default flash cards")}
                .collect {
                    Log.d("FLASH_CARD_VIEW_MODEL", "Default flash cards inserted successfully")
                    _flashCards.emit(FlashCard.getCards())
                }
        }
    }

    fun createFlashCard(question: String, answers: List<FlashCardAnswer>, correctAnswerId: Int) = viewModelScope.launch {
        val flashCard = FlashCard(
            id = Random.nextInt(0, Int.MAX_VALUE),
            question = question,
            answers = answers,
            correctAnswer = correctAnswerId,
            timestamp = System.currentTimeMillis())
        flashCardStorage.insert(flashCard).catch { Log.e("FLASH_CARD_VIEW_MODEL", "Could not insert flash card") }.collect()
        flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }.collect{_flashCards.emit(it)}
    }

    fun editFlashCard(cardId: Int, updatedFlashCard: FlashCard) = viewModelScope.launch {
        flashCardStorage.edit(cardId, updatedFlashCard).catch { Log.e("FLASH_CARD_VIEW_MODEL", "Could not edit flash card") }.collect()
        flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }.collect{_flashCards.emit(it)}
    }

    fun deleteFlashCard(cardId: Int) = viewModelScope.launch {
        if (cardId != null) {
            flashCardStorage.delete(cardId).catch { Log.e("FLASH_CARD_VIEW_MODEL", "Could not delete flash card") }.collect()
            flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }.collect{_flashCards.emit(it)}
        }

    }

}



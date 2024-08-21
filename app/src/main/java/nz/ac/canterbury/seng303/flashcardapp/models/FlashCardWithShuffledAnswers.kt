package nz.ac.canterbury.seng303.flashcardapp.models

data class FlashCardWithShuffledAnswers(
    val flashCard: FlashCard,
    val shuffledAnswers: List<FlashCardAnswer>
)

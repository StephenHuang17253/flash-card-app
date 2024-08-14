package nz.ac.canterbury.seng303.flashcardapp.models

data class FlashCard(
    val id: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int,
    val timestamp: Long,
): Identifiable {

    companion object {
        fun getCards(): List<FlashCard> {
            return listOf(
                FlashCard(
                    1,
                    "Which planet is known as the Red Planet?",
                    listOf("Mars", "Venus", "Jupiter"),
                    0,
                    System.currentTimeMillis()
                ),
                FlashCard(
                    2,
                    "Who wrote 'A Game of Thrones'?",
                    listOf("J.R.R Tolkien", "George R.R. Martin", "J.K. Rowling"),
                    1,
                    System.currentTimeMillis()
                ),
                FlashCard(
                    3,
                    "Which of these is the first book in the 'Stormlight Archive' series?",
                    listOf("Oathbringer", "The Way of Kings", "Words of Radiance", "Rhythm of War"),
                    1,
                    System.currentTimeMillis()
                )
            )
        }
    }

    override fun getIdentifier(): Int {
        return id;
    }
}


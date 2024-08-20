package nz.ac.canterbury.seng303.flashcardapp.models

data class FlashCard(
    val id: Int,
    val question: String,
    val answers: List<FlashCardAnswer>,
    val correctAnswer: Int,
    val timestamp: Long,
): Identifiable {

    companion object {
        fun getCards(): List<FlashCard> {
            return listOf(
                FlashCard(
                    1,
                    "Which planet is known as the Red Planet?",
                    answers = listOf(
                        FlashCardAnswer(0, "Mars"),
                        FlashCardAnswer(1, "Venus"),
                        FlashCardAnswer(2, "Jupiter")
                    ),
                    0,
                    System.currentTimeMillis()
                ),
                FlashCard(
                    2,
                    "Who wrote 'A Game of Thrones'?",
                    answers = listOf(
                        FlashCardAnswer(0, "J.R.R Tolkien"),
                        FlashCardAnswer(1, "George R.R. Martin"),
                        FlashCardAnswer(2, "J.K. Rowling")
                    ),
                    1,
                    System.currentTimeMillis()
                ),
                FlashCard(
                    3,
                    "Which of these is the first book in the 'Stormlight Archive' series?",
                    answers = listOf(
                        FlashCardAnswer(0, "Oathbringer"),
                        FlashCardAnswer(1, "The Way of Kings"),
                        FlashCardAnswer(2, "Words of Radiance"),
                        FlashCardAnswer(3, "Rhythm of War")
                    ),
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


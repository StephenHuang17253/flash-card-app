package nz.ac.canterbury.seng303.flashcardapp.models

data class FlashCardAnswer(
    val id : Int,
    var text: String
): Identifiable {

    override fun getIdentifier(): Int {
        return id;
    }

}



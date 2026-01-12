package com.example.fillerwordscounter.processing

data class WordCountResult(
    val totalWords: Int,
    val likeCount: Int,
    val umCount: Int,
    val basicallyCount: Int
)

object WordCounter {
    // "words" = letters + apostrophes, so "I'm" counts as one word
    private val wordRegex = Regex("[a-zA-Z']+")

    fun count(transcript: String): WordCountResult {
        val words = wordRegex.findAll(transcript.lowercase())
            .map { it.value }
            .toList()

        var like = 0
        var um = 0
        var basically = 0

        for (w in words) {
            when (w) {
                "like" -> like++
                "um" -> um++
                "basically" -> basically++
            }
        }

        return WordCountResult(
            totalWords = words.size,
            likeCount = like,
            umCount = um,
            basicallyCount = basically
        )
    }
}
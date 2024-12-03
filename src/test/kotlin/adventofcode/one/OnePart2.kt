package adventofcode.one

import adventofcode.readFile
import org.junit.jupiter.api.Test

class OnePart2 {
    @Test
    fun `real input`() {
        val input: Input = readFile("adventOfCode2024/one.txt").toInput()
        assert(input.similarityScore() == 22588371)
    }

    @Test
    fun `compute similarity score for two lists in the example`() {
        val similarityScore = listOf(4, 3, 5, 3, 9, 3).computeSimilarityScoreWith(listOf(3, 4, 2, 1, 3, 3))
        assert(similarityScore == 31)
    }

    @Test
    fun `compute similarity score for two lists`() {
        val similarityScore = listOf(4, 3, 5, 3, 9, 3).computeSimilarityScoreWith(listOf(3))

        assert(similarityScore == 3 * 3)
    }

    @Test
    fun `compute similarity score for one element`() {
        val similarityScore = listOf(4, 3, 5, 3, 9, 3).computeSimilarityScoreOf(3)

        assert(similarityScore == 3 * 3)
    }

    @Test
    fun `find occurrences`() {
        val occurrences = listOf(1, 2, 3, 3).findOccurrencesOf(3)

        assert(occurrences == 2)
    }
}

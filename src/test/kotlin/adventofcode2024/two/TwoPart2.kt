package adventofcode2024.two

import adventofcode2024.readFileAsList
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TwoPart2 {
    @Test
    fun `real input`() {
        val lines = readFileAsList("adventOfCode2024/two.txt")
        assert(lines.map(Line::from).countSafeIncludingDampener() == 665)
    }

    @Test
    fun `can count well including dampener`() {
        val input =
            listOf(
                Line(1, 2, 7, 8, 9), // unsafe
                Line(9, 7, 6, 2, 1), // unsafe
                Line(1, 3, 2, 4, 5), // safe by removing 3
                Line(8, 6, 4, 4, 1), // safe by removing 4
            )

        assert(input.countSafeIncludingDampener() == 2)
    }

    @Test
    fun `unsafe regardless of levels removed`() {
        assertFalse(Line(1, 2, 7, 8, 9).hasSafeSubsequence())
        assertFalse(Line(9, 7, 6, 2, 1).hasSafeSubsequence())
    }

    @Test
    fun `safe if one level is removed`() {
        assertTrue(Line(1, 3, 2, 4, 5).hasSafeSubsequence())
        assertTrue(Line(8, 6, 4, 4, 1).hasSafeSubsequence())
    }
}

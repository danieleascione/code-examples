package adventofcode.two

import adventofcode.readFileAsList
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * https://adventofcode.com/2024/day/2
 */
class TwoPart1 {
    @Test
    fun `should diff by at least one and at most three`() {
        assertTrue(Line(7, 6, 4, 2, 1).isSafe())
    }

    @Test
    fun `should increase or decrease`() {
        assertFalse(Line(6, 4, 4, 1).isSafe())
    }

    @Test
    fun `should diff by at least one and at most three is false`() {
        assertFalse(Line(1, 2, 7, 8, 9).isSafe())
        assertFalse(Line(9, 7, 6, 2, 1).isSafe())
    }

    @Test
    fun `is all increasing or all decreasing`() {
        assertTrue(Line(1, 2, 3).isSafe())
    }

    @Test
    fun `is all increasing or all decreasing should be false`() {
        assertFalse(Line(1, 3, 2, 4, 5).isSafe())
    }

    @Test
    fun `other examples`() {
        assertFalse(Line(8, 6, 4, 4, 1).isSafe())
        assertTrue(Line(1, 3, 6, 7, 9).isSafe())
    }

    @Test
    fun `count how many levels are safe`() {
        val input =
            emptyList<String>() +
                "7 6 4 2 1" +
                "1 2 7 8 9" +
                "9 7 6 2 1" +
                "1 3 2 4 5" +
                "8 6 4 4 1" +
                "1 3 6 7 9"
        val result = input.map(Line::from).countSafe()
        assert(result == 2)
    }

    @Test
    fun `should convert to list of integer`() {
        assert("1 2 4 7 9 8 \n".toIntList().toSet() == setOf(1, 2, 4, 7, 9, 8))
    }

    @Test
    fun `real input`() {
        val lines = readFileAsList("adventOfCode2024/two.txt")
        assert(lines.map(Line::from).countSafe() == 631)
    }
}

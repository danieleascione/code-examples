package advent_code.two

import advent_code.readFileAsList
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val DELIMITER = " "

/**
 * https://adventofcode.com/2024/day/2
 */
@Suppress("ktlint:standard:class-naming")
class AdventOfCode2024_2 {
    @Test
    fun `should diff by at least one and at most three`() {
        assertTrue(Level(7, 6, 4, 2, 1).isSafe())
    }

    @Test
    fun `should diff by at least one and at most three is false`() {
        assertFalse(Level(1, 2, 7, 8, 9).isSafe())
        assertFalse(Level(9, 7, 6, 2, 1).isSafe())
    }

    @Test
    fun `is all increasing or all decreasing`() {
        assertTrue(Level(1, 2, 3).isSafe())
    }

    @Test
    fun `is all increasing or all decreasing should be false`() {
        assertFalse(Level(1, 3, 2, 4, 5).isSafe())
    }

    @Test
    fun `other examples`() {
        assert(!Level(8, 6, 4, 4, 1).isSafe())
        assert(Level(1, 3, 6, 7, 9).isSafe())
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
        val result = input.map(Level::from).countSafe()
        assert(result == 2)
    }

    @Test
    fun `should convert to list of integer`() {
        assert("1 2 4 7 9 8 \n".toIntList().toSet() == setOf(1, 2, 4, 7, 9, 8))
    }

    @Test
    fun `real input`() {
        val lines = readFileAsList("adventOfCode2024/two.txt")
        assert(lines.map(Level::from).countSafe() == 631)
    }
}

fun List<Level>.countSafe() = count(Level::isSafe)

fun String.toIntList(): List<Int> = split(DELIMITER).mapNotNull(String::toIntOrNull)

fun String.toIntOrNull(): Int? = runCatching(::toInt).getOrNull()

data class Level(
    private val line: List<Int>,
) {
    constructor(vararg line: Int) : this(line.toList())

    companion object {
        fun from(singleLine: String): Level = Level(singleLine.toIntList())
    }

    fun isSafe() = differsByAtLeastOneAndAtMostThree() && isAllIncreasingOrAllDecreasing()

    private fun differsByAtLeastOneAndAtMostThree(): Boolean = adjacentDifferenceIsWithinRange(1..3)

    private fun isAllIncreasingOrAllDecreasing(): Boolean = isAllIncreasing() || isAllDecreasing()

    private fun isAllIncreasing() = sequenceOfCouples().all { (a, b) -> a < b }

    private fun isAllDecreasing() = sequenceOfCouples().all { (a, b) -> a > b }

    private fun adjacentDifferenceIsWithinRange(range: IntRange): Boolean = sequenceOfCouples().all { (a, b) -> abs(b - a) in range }

    private fun sequenceOfCouples() = line.asSequence().windowed(2)
}

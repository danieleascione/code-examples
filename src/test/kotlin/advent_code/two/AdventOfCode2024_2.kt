package advent_code.two

import advent_code.readFileAsList
import kotlin.math.abs
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

private const val DELIMITER = " "

/**
 * https://adventofcode.com/2024/day/2
 */
class AdventOfCode2024_2 {

    @Test
    fun `should diff by at least one and at most three`() {
        assertTrue(Level(intArrayOf(7, 6, 4, 2, 1)).differsByAtLeastOneAndAtMostThree())
    }
    @Test
    fun `should diff by at least one and at most three is false`() {
        assertFalse(Level(intArrayOf(1, 2, 7, 8, 9)).differsByAtLeastOneAndAtMostThree())
    }

    @Test
    fun `is all increasing or all decreasing`() {
        assertTrue(Level(intArrayOf(1, 2, 50)).isAllIncreasingOrAllDecreasing())
    }

    @Test
    fun `is all increasing or all decreasing should be false`() {
        assertFalse(Level(intArrayOf(1, 3, 2, 4, 5)).isAllIncreasingOrAllDecreasing())
    }

    @Test
    fun `examples`() {
        assert(intArrayOf(7, 6, 4, 2, 1).toLevel().isSafe())
        assert(!intArrayOf(1, 2, 7, 8, 9).toLevel().isSafe())
        assert(!intArrayOf(9, 7, 6, 2, 1).toLevel().isSafe())
        assert(!intArrayOf(8, 6, 4, 4, 1).toLevel().isSafe())
        assert(intArrayOf(1, 3, 6, 7, 9).toLevel().isSafe())
    }

    @Test
    fun `count how many are safe`() {
        val input =
            emptyList<String>() +
                "7 6 4 2 1" +
                "1 2 7 8 9" +
                "9 7 6 2 1" +
                "1 3 2 4 5" +
                "8 6 4 4 1" +
                "1 3 6 7 9"
        val result = input.map { Level.from(it) }.countSafe()
        assert(result == 2)
    }

    @Test
    fun `toIntArray should work`() {
         println("1 2 4 7 9 8\n".toIntArray().toList())
    }

    @Test
    fun `real input`() {
        val lines: List<String> = readFileAsList("adventOfCode2024/two.txt")
        println(lines.map { Level.from(it) }.countSafe())
    }
}

fun List<Level>.countSafe() = this.count { it.isSafe() }

fun IntArray.toLevel() = Level(this)

fun String.toIntArray(): IntArray {
    val a: List<String> = split(DELIMITER)
    val array = mutableListOf<Int>()
    for (i in a.indices) {
        if (!a[i].isInt()) break
        array += a[i].toInt()
    }
    return array.toIntArray()
}

fun String.isInt(): Boolean = try {
    toInt()
    true
} catch(e: NumberFormatException) {
    false
}

data class Level(val line: IntArray) {

    companion object {
        fun from(singleLine: String): Level =
            Level(singleLine.toIntArray())
    }

    fun isSafe() = differsByAtLeastOneAndAtMostThree() && isAllIncreasingOrAllDecreasing()

    fun differsByAtLeastOneAndAtMostThree(): Boolean = adjacentDifferByAtLeastOne() && adjacentDifferByAtMostThree()

    fun isAllIncreasingOrAllDecreasing(): Boolean =
        isAllIncreasing() != isAllDecreasing()

    fun isAllIncreasing() = line.asSequence().windowed(2).all { (a, b) -> a < b }
    fun isAllDecreasing() = line.asSequence().windowed(2).all { (a, b) -> a > b }


    private fun adjacentDifferByAtLeastOne(): Boolean {
        for(i in 0 until line.size - 1) {
            val diff = abs(line[i + 1] - line[i])
            if (diff < 1) {
                return false
            }
        }
        return true
    }

    private fun adjacentDifferByAtMostThree(): Boolean {
        for (i in 0 until line.size - 1) {
            val diff = abs(line[i + 1] - line[i])
            if (diff > 3) {
                return false
            }
        }
        return true
    }
}
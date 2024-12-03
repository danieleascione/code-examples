package adventofcode2024.two

import kotlin.math.abs

private const val DELIMITER = " "

fun List<Line>.countSafeIncludingDampener() =
    count {
        it.isSafe() || it.hasSafeSubsequence()
    }

fun List<Line>.countSafe() = count(Line::isSafe)

fun String.toIntList(): List<Int> = split(DELIMITER).mapNotNull(String::toIntOrNull)

fun String.toIntOrNull(): Int? = runCatching(::toInt).getOrNull()

data class Line(
    private val levels: List<Int>,
) {
    constructor(vararg levels: Int) : this(levels.toList())

    companion object {
        fun from(singleLine: String): Line = Line(singleLine.toIntList())
    }

    fun isSafe(): Boolean {
        val result = differsByAtLeastOneAndAtMostThree() && isAllIncreasingOrAllDecreasing()
        if (result) {
            println("$this is safe")
        }
        return result
    }

    fun hasSafeSubsequence() =
        levels
            .asSequence()
            .mapIndexed { index, _ -> withoutElementAtLevel(index).isSafe() }
            // TODO: Somehow it should be possible to use a lazy evaluation with a sequence and exit immediately if we have one true result
            // Given the bruteforce solution I am not bothered with the optimisation at this point
            .any { it }

    private fun withoutElementAtLevel(levelToDiscard: Int): Line {
        val newList =
            buildList {
                levels.forEachIndexed { i, el -> if (i != levelToDiscard) add(el) }
            }
        return Line(newList)
    }

    private fun differsByAtLeastOneAndAtMostThree(): Boolean = adjacentDifferenceIsWithinRange(1..3)

    private fun isAllIncreasingOrAllDecreasing(): Boolean = isAllIncreasing() || isAllDecreasing()

    private fun isAllIncreasing() =
        sequenceOfCouples().all { (a, b) ->
            a < b
        }

    private fun isAllDecreasing() =
        sequenceOfCouples().all { (a, b) ->
            a > b
        }

    private fun adjacentDifferenceIsWithinRange(range: IntRange): Boolean =
        sequenceOfCouples().all { (a, b) ->
            abs(b - a) in range
        }

    private fun sequenceOfCouples() = levels.asSequence().windowed(2)
}

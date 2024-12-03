package adventofcode2024.one

import java.io.InputStream
import kotlin.math.abs

private const val DELIMITER = "   "

data class Input(
    val array1: List<Int>,
    val array2: List<Int>,
) {
    operator fun plus(other: Input) = Input(other.array1 + array1, other.array2 + array2)

    fun distance(): Int = distanceBetween(array1, array2)

    fun similarityScore() = array1.computeSimilarityScoreWith(array2)
}

fun distanceBetween(
    array1: List<Int>,
    array2: List<Int>,
): Int {
    require(array2.size == array1.size)

    val sortedList1 = array1.sorted()
    val sortedList2 = array2.sorted()

    return sortedList1.zip(sortedList2).fold(0) { sum: Int, (a: Int, b: Int) -> sum + abs(a - b) }
}

internal fun InputStream.toInput(): Input = readArrays(bufferedReader().readLines().map { asInput(it) })

private fun asInput(string: String): Input = string.split(DELIMITER).let { Input(listOf(it[0].toInt()), listOf(it[1].toInt())) }

private fun readArrays(input: List<Input>): Input = input.fold(Input(emptyList(), emptyList())) { a: Input, b: Input -> a + b }

fun List<Int>.computeSimilarityScoreWith(value: List<Int>) = value.sumOf { computeSimilarityScoreOf(it) }

fun List<Int>.computeSimilarityScoreOf(value: Int) = value * findOccurrencesOf(value)

fun List<Int>.findOccurrencesOf(value: Int) = count { it == value }

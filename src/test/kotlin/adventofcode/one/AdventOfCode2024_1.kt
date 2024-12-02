package adventofcode.one

import java.io.InputStream
import kotlin.math.abs
import kotlin.test.Test

private const val DELIMITER = "   "

@Suppress("ktlint:standard:class-naming")
class AdventOfCode2024_1 {
    @Test
    fun `real input`() {
        val input: Input = readFile("adventOfCode2024/one.txt").toInput()
        println(input.distance())
    }

    @Test
    fun `should work`() {
        assert(distanceBetween(intArrayOf(1, 2), intArrayOf(1, 2)) == 0)
    }

    @Test
    fun `should work 2`() {
        val dist = distanceBetween(intArrayOf(5, 10), intArrayOf(1, 20))
        assert(dist == 14)
    }

    @Test
    fun `should read arrays`() {
        val inputAfterRead: Input = readArrays(listOf("5${DELIMITER}10", "1${DELIMITER}20").map { asInput(it) })

        println(inputAfterRead)

        assert(inputAfterRead.array1.contentEquals(intArrayOf(5, 1)))
        assert(inputAfterRead.array1.contentEquals(intArrayOf(1, 20)))
    }

    @Test
    fun `split should work`() {
        val split = asInput("1${DELIMITER}5")
        println(split)
        assert(split == Input(intArrayOf(1), intArrayOf(5)))
    }

    fun readFile(filename: String): InputStream = javaClass.getClassLoader().getResourceAsStream(filename)!!

    private fun InputStream.toInput(): Input = readArrays(bufferedReader().readLines().map { asInput(it) })

    private fun asInput(string: String): Input = string.split(DELIMITER).let { Input(intArrayOf(it[0].toInt()), intArrayOf(it[1].toInt())) }

    private fun readArrays(input: List<Input>): Input = input.fold(Input(intArrayOf(), intArrayOf())) { a: Input, b: Input -> a + b }

    data class Input(
        val array1: IntArray,
        val array2: IntArray,
    ) {
        fun add(
            one: Int,
            two: Int,
        ): Input = Input(array1.plus(one), array2.plus(two))

        operator fun plus(other: Input) = Input(other.array1.plus(array1), other.array2.plus(array2))

        fun distance(): Int = distanceBetween(array1, array2)
    }
}

fun distanceBetween(
    array1: IntArray,
    array2: IntArray,
): Int {
    require(array2.size == array1.size)

    array1.sort()
    array2.sort()

    return array1.zip(array2).fold(0) { sum: Int, (a: Int, b: Int) -> sum + abs(a - b) }
}

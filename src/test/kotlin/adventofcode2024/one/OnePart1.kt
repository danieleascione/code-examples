package adventofcode2024.one

import adventofcode2024.readFile
import kotlin.test.Test

class OnePart1 {
    @Test
    fun `real input`() {
        val input: Input = readFile("adventOfCode2024/one.txt").toInput()
        assert(input.distance() == 1590491)
    }

    @Test
    fun `should work`() {
        assert(distanceBetween(listOf(1, 2), listOf(1, 2)) == 0)
    }

    @Test
    fun `should work 2`() {
        assert(distanceBetween(listOf(5, 10), listOf(1, 20)) == 14)
    }
}

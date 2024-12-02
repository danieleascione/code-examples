package advent_code

import advent_code.one.AdventOfCode2024_1
import java.io.InputStream

class Utils

fun readFile(filename: String): InputStream = Utils().javaClass.getClassLoader().getResourceAsStream(filename)!!
fun readFileAsList(filename: String): List<String> = Utils().javaClass.getClassLoader().getResourceAsStream(filename)!!.bufferedReader().readLines()

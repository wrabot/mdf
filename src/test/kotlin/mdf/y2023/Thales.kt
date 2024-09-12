package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.read.readLines
import tools.toPoint
import kotlin.math.min

class Thales : BaseTest() {
    @Test
    fun test1() = test(17, ::p1)

    @Test
    fun test2() = test(10, ::p2)

    @Test
    fun test3() = test(11, ::p3)

    @Test
    fun test4() = test(10, ::p4)

    private fun p1() = println(readLines(readln().toInt()).count { it.toPoint(" ").norm2() < 10000 })

    private fun p2() {
        val result = readLines(readln().toInt())
            .indexOfFirst { it.replace('_', '.').toRegex().matches("ALIMENTATION") } + 1
        println(result)
    }

    private fun p3() {
        val input = readLines(readln().toInt()).map { line ->
            line.split(" - ").let { it[0].toMinutes() until it[1].toMinutes() }
        }
        val result = List(input.size) { index ->
            val other = input.get(index)
            input.drop(index + 1).count { (it intersect other).size >= 15 }
        }.sum()
        println(result)
    }

    private fun String.toMinutes() = split(":").let { it[0].toInt() * 60 + it[1].toInt() }

    private fun p4() {
        readln()
        println(readln().split(" ").allGenes(0).minBy { it.length })
    }

    private fun List<String>.allGenes(index: Int): List<String> = if (index >= size) listOf("") else {
        val prefix = get(index)
        val reversed = prefix.reversed()
        allGenes(index + 1).flatMap { listOf(prefix.minConcat(it), reversed.minConcat(it)) }
    }

    private fun String.minConcat(other: String) = (min(length, other.length) downTo 0)
        .first { other.startsWith(substring(length - it)) }.let { this + other.drop(it) }
}

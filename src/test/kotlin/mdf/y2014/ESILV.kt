package mdf.y2014

import mdf.BaseTest
import org.junit.Test
import tools.board.Direction4
import tools.board.XY

class ESILV : BaseTest() {
    @Test
    fun test1() = test(1, ::p1)

    @Test
    fun test2() = test(4, ::p2)

    @Test
    fun test3() = test(1, ::p3)

    @Test
    fun test4() = test(1, ::p4)

    private fun p1(lines: List<String>) = lines[0].split(" ").map { it.toInt(16) }.run {
        indices.map {
            val v = get(it)
            drop(it).withIndex().takeWhile { it.value == v + it.index }.map { it.value }
        }.maxBy { it.size }
    }.joinToString(" ") { it.toString(16) }.uppercase()

    private fun p2(lines: List<String>): String {
        val n = lines[0].toInt() / 2
        return lines.drop(1).flatMapIndexed { r, line ->
            val y = n - r
            line.split(" ").mapIndexed { c, s ->
                val x = c - n
                Triple(s.toInt(), x.square() + y.square(), "$x $y")
            }
        }.sortedBy { it.second }.maxBy { it.first }.third
    }

    private fun Int.square() = this * this

    private fun p3(lines: List<String>): String {
        val values = lines[0].split(" ").chunked(2) { it.first().first() to it.last().toInt() }.toMap()
        return lines.drop(1).map { it to it.sumOf { values[it] ?: 0 } }
            .sortedByDescending { it.second }.joinToString("\n") { it.first }
    }

    private fun p4(lines: List<String>): String {
        val directions =
            mapOf('O' to Direction4.West, 'N' to Direction4.North, 'E' to Direction4.East, 'S' to Direction4.South)
        val n = lines[0].toInt()
        return lines[1].windowed(n).associateWith {
            it.runningFold(XY(0, 0)) { acc, c -> acc + directions[c]!!.xy }.distinct().size
        }.maxBy { it.value }.run { "$value\n$key" }
    }
}

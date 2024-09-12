package mdf.y2014

import mdf.BaseTest
import org.junit.Test
import tools.board.Direction4
import tools.XY
import tools.read.readAllLines

class ESILV : BaseTest() {
    @Test
    fun test1() = test(1, ::p1)

    @Test
    fun test2() = test(4, ::p2)

    @Test
    fun test3() = test(1, ::p3)

    @Test
    fun test4() = test(1, ::p4)

    private fun p1() {
        val result = readln().split(" ").map { it.toInt(16) }.run {
            indices.map { index ->
                val v = get(index)
                drop(index).withIndex().takeWhile { it.value == v + it.index }.map { it.value }
            }.maxBy { it.size }
        }.joinToString(" ") { it.toString(16) }.uppercase()
        println(result)
    }

    private fun p2() {
        val n = readln().toInt() / 2
        val result = readAllLines().flatMapIndexed { r, line ->
            val y = n - r
            line.split(" ").mapIndexed { c, s ->
                val x = c - n
                Triple(s.toInt(), x.square() + y.square(), "$x $y")
            }
        }.sortedBy { it.second }.maxBy { it.first }.third
        println(result)
    }

    private fun Int.square() = this * this

    private fun p3() {
        val values = readln().split(" ").chunked(2) { it.first().first() to it.last().toInt() }.toMap()
        val result = readAllLines().map { line -> line to line.sumOf { values[it] ?: 0 } }
            .sortedByDescending { it.second }.joinToString("\n") { it.first }
        println(result)
    }

    private fun p4() {
        val directions =
            mapOf('O' to Direction4.West, 'N' to Direction4.North, 'E' to Direction4.East, 'S' to Direction4.South)
        val n = readln().toInt()
        val result = readln().windowed(n).associateWith {
            it.runningFold(XY(0, 0)) { acc, c -> acc + directions[c]!!.xy }.distinct().size
        }.maxBy { it.value }.run { "$value\n$key" }
        println(result)
    }
}

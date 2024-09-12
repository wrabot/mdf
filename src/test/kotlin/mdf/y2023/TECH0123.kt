package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines

class TECH0123 : BaseTest() {
    @Test
    fun test1() = test(0, ::p1)

    @Test
    fun test2() = test(0, ::p2)

    @Test
    fun test3() = test(4, ::p3)

    private fun p1() {
        val result = readln().split(" ").map { it.toInt() }.groupingBy { it }.eachCount().toList()
            .sortedBy { it.first }.sortedBy { it.second }.run { last().first - first().first }
        println(result)
    }

    private fun p2() {
        val officers = readln().toInt()
        val teams = readAllLines().drop(1).map { it.toInt() }
        val needed = (teams.sum() + officers) / 2 + 1 - officers
        if (needed <= 0) {
            println(0)
        } else {
            val sums = mutableSetOf<Int>()
            teams.forEach { team ->
                sums += sums.map { it + team } + team
            }
            println(sums.filter { it >= needed }.min())
        }
    }

    private fun p3() {
        val destination = readln().split(" ").map { it.toLong() }.let {
            Point(it[0], it[1])
        }
        val moves = readln().map {
            when (it) {
                'U' -> Point(0, 1)
                'R' -> Point(1, 0)
                'D' -> Point(0, -1)
                'L' -> Point(-1, 0)
                else -> error("unexpected")
            }
        }
        val positions = moves.runningFold(Point(0, 0)) { position, move -> position + move }
        val modulo = positions.last()
        val result = positions.dropLast(1).mapIndexedNotNull { index, value ->
            val delta = destination - value
            val rx = delta.x % modulo.x
            val ry = delta.y % modulo.y
            val nx = delta.x / modulo.x
            val ny = delta.y / modulo.y
            if (rx == 0L && ry == 0L && nx == ny && nx >= 0) nx * (positions.size - 1) + index else null
        }.minOrNull()
        println(result ?: "not possible")
    }

    data class Point(val x: Long, val y: Long) {
        operator fun minus(other: Point) = Point(x - other.x, y - other.y)
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    }
}

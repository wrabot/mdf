package mdf

import org.junit.Test

class TECH20230123 : BaseTest() {
    @Test
    fun test1() = test(0, ::p1)

    @Test
    fun test2() = test(0, ::p2)

    @Test
    fun test3() = test(4, ::p3)

    private fun p1(lines: List<String>): Any {
        return lines[0].split(" ").map { it.toInt() }.groupingBy { it }.eachCount().toList()
            .sortedBy { it.first }.sortedBy { it.second }.run { last().first - first().first }
    }

    private fun p2(lines: List<String>): Any {
        val officers = lines[0].toInt()
        val teams = lines.drop(2).map { it.toInt() }
        val needed = (teams.sum() + officers) / 2 + 1 - officers
        if (needed <= 0) return 0
        val sums = mutableSetOf<Int>()
        teams.forEach { team ->
            sums += sums.map { it + team } + team
        }
        return sums.filter { it >= needed }.min()
    }

    private fun p3(lines: List<String>): Any {
        val destination = lines[0].split(" ").map { it.toLong() }.let {
            Point(it[0], it[1])
        }
        val moves = lines[1].map {
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
        return positions.dropLast(1).mapIndexedNotNull { index, value ->
            val delta = destination - value
            val rx = delta.x % modulo.x
            val ry = delta.y % modulo.y
            val nx = delta.x / modulo.x
            val ny = delta.y / modulo.y
            if (rx == 0L && ry == 0L && nx == ny && nx >= 0) nx * (positions.size - 1) + index else null
        }.minOrNull() ?: "not possible"
    }

    data class Point(val x: Long, val y: Long) {
        operator fun minus(other: Point) = Point(x - other.x, y - other.y)
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    }
}

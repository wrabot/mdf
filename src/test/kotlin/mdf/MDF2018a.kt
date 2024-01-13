package mdf

import org.junit.Test
import tools.board.Board
import tools.board.Point
import kotlin.math.roundToLong

class MDF2018a : BaseTest() {
    // println(p(lines))

    @Test
    fun test1() = test(5, ::p1)

    @Test
    fun test2() = test(8, ::p2)

    @Test
    fun test3() = test(6, ::p3)


    private fun p1(lines: List<String>) = lines.drop(1).map { it.toInt() }.runningFold(0 to 0) { acc, i ->
        val s = acc.first + i
        s to if (s < 0) acc.second + 1 else 0
    }.drop(1).run {
        sumOf { if (it.second < 3) 0.0 else it.first * 0.1 } - sumOf {
            when {
                it.second == 0 -> 0.0
                it.second < 4 -> it.first * 0.2
                else -> it.first * 0.3
            }
        }
    }.roundToLong()

    private fun p2(lines: List<String>) = lines.drop(1).map { line ->
        val (name, height, result) = line.split(" ")
        Triple(name, height.toDouble(), result == "S")
    }.run {
        val maxHeight = maxOf { if (it.third) it.second else 0.0 }
        val counts = filter { it.second == maxHeight }.groupingBy { it.first to it.second }.eachCount()
        val minTries = counts.minOf { it.value }
        counts.filter { it.value == minTries }
    }.keys.singleOrNull()?.first ?: "KO"

    data class Cell(var c: Char) {
        override fun toString() = c.toString()
    }

    private fun p3(lines: List<String>) = lines[0].toInt().let { size ->
        val petri = Board(size, size, lines.drop(1).flatMap { line -> line.map { Cell(it) } })
        val bacteria = petri.cells.filter { it.c.isDigit() }.map { it.c }.distinct()
        do {
            isModified = false
            petri.points.filter { petri[it].c == '=' }.forEach { petri[petri.neighbors4(it)] = '=' }
            val contamination = bacteria.associateWith { b ->
                petri.points.filter { petri[it].c == b }.flatMap { petri.neighbors4(it) }.distinct()
            }
            petri[contamination.values.flatten().groupingBy { it }.eachCount().filter { it.value > 1 }.keys] = '='
            contamination.forEach { (b, points) -> petri[points] = b }
        } while (isModified)
        bacteria.maxOf { b -> petri.points.count { petri[it].c == b } }
    }

    private var isModified = false
    private operator fun Board<Cell>.set(points: Iterable<Point>, b: Char) = points.forEach {
        val cell = this[it]
        if (cell.c == '.') {
            cell.c = b
            isModified = true
        }
    }
}

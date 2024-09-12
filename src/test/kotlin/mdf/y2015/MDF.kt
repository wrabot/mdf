package mdf.y2015

import mdf.BaseTest
import org.junit.Test
import tools.board.toBoard
import tools.read.readAllLines
import tools.text.toInts

class MDF : BaseTest() {
    @Test
    fun test1() = test(2, ::p1)

    @Test
    fun test2() = test(2, ::p2)

    @Test
    fun test3() = test(2, ::p3)

    @Test
    fun test4() = test(2, ::p4)

    @Test
    fun test5() = test(2, ::p5)

    @Test
    fun test6() = test(1, ::p6)

    @Test
    fun test7() = test(2, ::p7)

    private fun p1() {
        var i = readln().toInt()
        val size = readln().toInt()
        repeat(size) { 
            val (x, y) = readln().toInts()
            i += y - x
        }
        println(i)
    }

    private fun p2() {
        val result = readAllLines().drop(1).groupingBy { it }.eachCount().toList()
            .sortedByDescending { it.second }.take(5).joinToString("\n") { "${it.first} ${it.second}" }
        println(result)
    }

    private fun p3() {
        readln()
        val countries = readln().split(";").toSet()
        val entries = readAllLines().map { it.split(";") }
        val distincts = entries.distinctBy { it.take(3) }
        val duplicates = entries.size - distincts.size
        val regex = "\\+\\d{1,3}-\\d{9,11}".toRegex()
        val invalidNumber = distincts.count { !regex.matches(it[3]) }
        val invalidCountries = distincts.count { it.last() !in countries }
        println("$duplicates $invalidNumber $invalidCountries")
    }

    private fun p4() {
        val result = readAllLines().drop(1).windowed(60, 1).firstNotNullOfOrNull { w ->
            w.groupingBy { it }.eachCount().firstNotNullOfOrNull { if (it.value >= 40) it.key else null }
        }
        println(result ?: "Pas de trending topic")
    }

    private fun p5() {
        val (identical, different) = readAllLines().drop(1).map {
            val (t, l) = it.split(" ")
            Triple(t.first(), l.toInt(), t.last())
        }.sortedByDescending { it.second }.partition { it.first == it.third }
        val (m, f) = identical.partition { it.first == 'M' }
        println(different.sumOf { it.second } + m.zip(f) { a, b -> a.second + b.second }.sum())
    }

    // FIXME timeout on server 
    private fun p6() {
        var rectangles = readAllLines().drop(1).map {
            val (l, t, r, b) = it.split(" ").map(String::toInt)
            Rect(l..r, t..b)
        }
        var duration = 0
        while (rectangles.isNotEmpty()) {
            rectangles = rectangles.flatMap { r ->
                val x = r.xRange.first
                val y = r.yRange.first
                if (rectangles.any { it.isInside(x - 1, y) || it.isInside(x, y - 1) }) listOf(r)
                else listOf(
                    Rect(r.xRange.first + 1..r.xRange.last, r.yRange.first..r.yRange.first),
                    Rect(r.xRange, r.yRange.first + 1..r.yRange.last)
                )
            }.filterNot { it.isEmpty() } + rectangles.flatMap { r ->
                val y = r.yRange.last + 1
                rectangles.mapNotNull {
                    val x = it.xRange.last + 1
                    if (y in it.yRange && x in r.xRange) Rect(x..x, y..y) else null
                }
            }
            duration++
        }
        println(duration)
    }

    private data class Rect(val xRange: IntRange, val yRange: IntRange) {
        fun isInside(x: Int, y: Int) = x in xRange && y in yRange
        fun isEmpty() = xRange.isEmpty() || yRange.isEmpty()
    }

    private fun p7() {
        val board = readAllLines().drop(1).toBoard { Cell(it) }
        do {
            var isModified = false
            board.xy.forEach { xy ->
                val min = board.neighbors4(xy).minOf { board[it].depth } + 1
                if (min < board[xy].depth) {
                    board[xy].depth = min
                    isModified = true
                }
            }
        } while (isModified)
        println(board.cells.maxOf { it.depth })
    }

    private data class Cell(val c: Char, var depth: Int = if (c == '#') Int.MAX_VALUE else 0)
}

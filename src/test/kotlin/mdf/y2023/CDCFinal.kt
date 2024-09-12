package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines
import tools.toPoint

class CDCFinal : BaseTest() {
    @Test
    fun test1() = test(14, ::p1)

    @Test
    fun test2() = test(15, ::p2)

    @Test
    fun test3() = test(14, ::p3)

    @Test
    fun test4() = test(11, ::p4)

    private fun p1() {
        val input = readln().toList()
        println(
            input.sorted().toMutableList().apply {
                val i = indexOfFirst { it != '0' }
                if (i > 0) add(0, removeAt(i))
            }.joinToString("")
        )
        println(input.sortedDescending().joinToString(""))
    }

    private fun p2() {
        val start = readln()
        val end = readln()
        val transits = readAllLines().drop(2).map { it.split(" ").toSet() }
        println(transits.first { start in it }.intersect(transits.first { end in it }).single())
    }

    private fun p3() {
        val center = readln().toPoint(" ")
        val radius2 = readln().toDouble().let { it * it }
        var count = 0
        var consecutive = 0
        readAllLines().drop(1).forEach {
            if ((it.toPoint(" ") - center).norm2() <= radius2) {
                if (++consecutive == 2) count++
            } else {
                consecutive = 0
            }
        }
        println(count)
    }

    private fun p4() {
        readln()
        val rows = readln().alternateSum()
        val columns = readln().alternateSum()
        val p = rows.first * columns.first + rows.second * columns.second
        val v = rows.second * columns.first + rows.first * columns.second
        println("$p $v")
    }

    private fun String.alternateSum() = split(" ").chunked(2).fold(0L to 0L) { acc, s ->
        acc.first + s[0].toLong() to acc.second + (s.getOrNull(1)?.toLong() ?: 0L)
    }
}

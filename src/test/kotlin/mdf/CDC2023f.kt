package mdf

import org.junit.Test
import tools.geometry.toPoint

class CDC2023f : BaseTest() {
    @Test
    fun test1() = test(14, ::p1)

    @Test
    fun test2() = test(15, ::p2)

    @Test
    fun test3() = test(14, ::p3)

    @Test
    fun test4() = test(11, ::p4)

    private fun p1(lines: List<String>) = lines[0].toList().run {
        listOf(
            sorted().toMutableList().apply {
                val i = indexOfFirst { it != '0' }
                if (i > 0) add(0, removeAt(i))
            },
            sortedDescending()
        ).joinToString("\n") { it.joinToString("") }
    }

    private fun p2(lines: List<String>): Any {
        val start = lines[0]
        val end = lines[1]
        val transits = lines.drop(4).map { it.split(" ").toSet() }
        return transits.first { start in it }.intersect(transits.first { end in it }).single()
    }

    private fun p3(lines: List<String>): Any {
        val center = lines[0].toPoint()
        val radius2 = lines[1].toDouble().let { it * it }
        var count = 0
        var consecutive = 0
        lines.drop(3).forEach {
            if ((it.toPoint() - center).norm2() <= radius2) {
                if (++consecutive == 2) count++
            } else {
                consecutive = 0
            }
        }
        return count
    }

    private fun p4(lines: List<String>): String {
        val rows = lines[1].alternateSum()
        val columns = lines[2].alternateSum()
        return "${rows.first * columns.first + rows.second * columns.second} ${rows.second * columns.first + rows.first * columns.second}"
    }

    private fun String.alternateSum() = split(" ").chunked(2).fold(0L to 0L) { acc, s ->
        acc.first + s[0].toLong() to acc.second + (s.getOrNull(1)?.toLong() ?: 0L)
    }
}

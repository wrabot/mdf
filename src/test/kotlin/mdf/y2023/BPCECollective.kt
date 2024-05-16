package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.board.toBoard
import tools.graph.shortPath
import tools.sequence.select

class BPCECollective : BaseTest() {
    @Test
    fun test1() = test(10, ::p1)

    @Test
    fun test2() = test(10, ::p2)

    @Test
    fun test3() = test(7, ::p3)

    private fun p1(lines: List<String>) = lines.drop(1).joinToString("\n") {
        it.zip(it.reversed()).joinToString("") { (a, b) -> if (a == '#' || b == '#') "#" else "." }
    }

    private fun p2(lines: List<String>) = lines.map(String::toDouble).select(9).map { it.average() }.toList().run {
        listOf(min(), average(), median(), max())
    }.joinToString("\n")

    private fun List<Double>.median() = sorted().let {
        if (size % 2 == 0) (it[size / 2 - 1] + it[size / 2]) / 2 else it[size / 2]
    }

    private fun p3(lines: List<String>): Int {
        val map = lines.drop(1).toBoard { it }
        return shortPath(
            map.cells.indexOf('A'),
            map.cells.indexOf('B'),
            cost = { _, b -> if (map.cells[b] == '#') 1.0 else 0.0 }
        ) {
            map.neighbors4(map.xy[it]).map { map.indexOf(it)!! }
        }.zipWithNext { _, b -> if (map.cells[b] == '#') 1.0 else 0.0 }.sum().toInt()
    }
}

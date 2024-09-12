package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.board.toBoard
import tools.graph.shortPath
import tools.read.readAllLines
import tools.sequence.select

class BPCECollective : BaseTest() {
    @Test
    fun test1() = test(10, ::p1)

    @Test
    fun test2() = test(10, ::p2)

    @Test
    fun test3() = test(7, ::p3)

    private fun p1() {
        val result = readAllLines().drop(1).joinToString("\n") {
            it.zip(it.reversed()).joinToString("") { (a, b) -> if (a == '#' || b == '#') "#" else "." }
        }
        println(result)
    }

    private fun p2() {
        val result = readAllLines().map(String::toDouble).select(9).map { it.average() }.toList()
            .run { listOf(min(), average(), median(), max()) }
        println(result.joinToString("\n"))
    }

    private fun List<Double>.median() = sorted().let {
        if (size % 2 == 0) (it[size / 2 - 1] + it[size / 2]) / 2 else it[size / 2]
    }

    private fun p3() {
        val map = readAllLines().drop(1).toBoard { it }
        val path = shortPath(
            map.cells.indexOf('A'),
            map.cells.indexOf('B'),
            cost = { _, b -> map.cells[b].cost() }
        ) {
            map.neighbors4(map.xy[it]).map { map.indexOf(it)!! }
        }
        println(path.zipWithNext { _, b -> map.cells[b].cost() }.sum().toInt())
    }

    private fun Char.cost() = if (this == '#') 1.0 else 0.0
}

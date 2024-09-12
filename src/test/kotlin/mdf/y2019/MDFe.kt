package mdf.y2019

import mdf.BaseTest
import org.junit.Test
import tools.Point
import tools.board.toBoard
import tools.graph.zone
import tools.read.readAllLines
import tools.text.toInts
import tools.toPoint

class MDFe : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(4, ::p2)

    @Test
    fun test3() = test(15, ::p3)

    private fun p1() {
        val my = readln().toInt()
        val actions = readAllLines().drop(1).map { line ->
            line.split(" ").let {
                it[0].toInt() to (it[1] == "D")
            }
        }
        val positions = (1..20).toMutableList()
        actions.forEach { (id, d) ->
            val index = positions.indexOf(id)
            positions.removeAt(index)
            if (d) positions.add(index - 1, id)
        }
        val final = positions.indexOf(my)
        println(if (final == -1) "KO" else final + 1)
    }

    private fun p2() {
        val board = readAllLines().drop(1).toBoard { it }
        val points = board.xy.filter { board[it] == 'X' }.toMutableList()
        var count = 0
        while (points.isNotEmpty()) {
            points.removeAll(zone(points.first()) { xy ->
                board.neighbors4(xy).filter { board[it] == 'X' }
            })
            count++
        }
        println(count)
    }

    private fun p3() {
        val d2 = readln().toInts()[1].let { it * it }
        val nodes = readAllLines().map { it.toPoint(" ") }
        val neighbors = nodes.associateWith { point ->
            (nodes - point).filter { (it - point).run { x * x + y * y } <= d2 }
        }
        println(longPath(listOf(nodes.first())) { neighbors[it].orEmpty() })
    }

    private fun longPath(path: List<Point>, neighbors: (Point) -> List<Point>): Int =
        neighbors(path.last()).filter { it !in path }
            .maxOfOrNull { 1 + longPath(path + it, neighbors) } ?: 0
}

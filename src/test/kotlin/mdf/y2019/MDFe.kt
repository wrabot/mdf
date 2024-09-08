package mdf.y2019

import mdf.BaseTest
import org.junit.Test
import tools.Point
import tools.board.toBoard
import tools.graph.zone
import tools.toPoint

class MDFe : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(4, ::p2)

    @Test
    fun test3() = test(15, ::p3)

    private fun p1(lines: List<String>): Any {
        val my = lines[0].toInt()
        val actions = lines.drop(2).map { line ->
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
        return if (final == -1) "KO" else final + 1
    }

    private fun p2(lines: List<String>): Any {
        val board = lines.drop(1).toBoard { it }
        val points = board.xy.filter { board[it] == 'X' }.toMutableList()
        var count = 0
        while (points.isNotEmpty()) {
            points.removeAll(zone(points.first()) { xy ->
                board.neighbors4(xy).filter { board[it] == 'X' }
            })
            count++
        }
        return count
    }

    private fun p3(lines: List<String>): Any {
        val d2 = lines[0].split(" ")[1].toInt().let { it * it }
        val nodes = lines.drop(1).map { it.toPoint(" ") }
        val neighbors = nodes.associateWith { point ->
            (nodes - point).filter { (it - point).run { x * x + y * y } <= d2 }
        }
        return longPath(listOf(nodes.first())) { neighbors[it].orEmpty() }
    }

    private fun longPath(path: List<Point>, neighbors: (Point) -> List<Point>): Int =
        neighbors(path.last()).filter { it !in path }
            .maxOfOrNull { 1 + longPath(path + it, neighbors) } ?: 0
}

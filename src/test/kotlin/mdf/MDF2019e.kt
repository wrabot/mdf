package mdf

import Board
import Point
import org.junit.Test
import toPoint

class MDF2019e : BaseTest("MDF2019e") {
    // println(p(lines))

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
        val rows = lines.drop(1)
        val board = Board(rows[0].length, rows.size, rows.flatMap { it.toList() })
        val points = board.points.filter { board[it] == 'X' }.toMutableList()
        var count = 0
        while (points.isNotEmpty()) {
            points.removeAll(board.zone4(points.first()) { board[it] == 'X' })
            count++
        }
        return count
    }

    private fun p3(lines: List<String>): Any {
        val d = lines[0].split(" ")[1].toInt()
        val d2 = d * d
        val nodes = lines.drop(1).map { it.toPoint() }
        val neighbors = nodes.associateWith { point ->
            (nodes - point).filter { (it - point).norm2() <= d2 }
        }
        return longPath(listOf(nodes.first())) { neighbors[it].orEmpty() }
    }

    private fun longPath(path: List<Point>, neighbors: (Point) -> List<Point>): Int =
        neighbors(path.last()).filter { it !in path }
            .maxOfOrNull { 1 + longPath(path + it, neighbors) } ?: 0
}

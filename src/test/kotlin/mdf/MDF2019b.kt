package mdf

import PointF
import org.junit.Test
import smallestCircle
import tools.board.Board
import tools.board.Point
import kotlin.math.ceil

class MDF2019b : BaseTest("MDF2019b") {
    // println(p(lines))

    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(21, ::p2)

    @Test
    fun test3() = test(9, ::p3)


    private fun p1(lines: List<String>): Any {
        return lines.drop(1).joinToString("").split("X").maxOfOrNull { it.count { it == 'B' } }!! - 1
    }

    private fun p2(lines: List<String>): Any {
        val board = Board(8, 8, lines.flatMap { it.toList() })
        val king = board.points.first { board[it] == 'R' }
        val free = board.neighbors8(king).any { !board.isCheck(it) }
        return if (free) "still-in-game" else if (board.isCheck(king)) "check-mat" else "pat"
    }

    private fun Board<Char>.isCheck(point: Point) = (0..7).any {
        it != point.y && get(point.x, it) == 'T'
    } || (0..7).any {
        it != point.x && get(it, point.y) == 'T'
    }

    // FIXME outputs 2-8 on website are wrong !!!
    private fun p3(lines: List<String>): Any {
        val points = lines.drop(1).map { it.split(" ").let { (x, y) -> PointF(x.toFloat(), y.toFloat()) } }
        return smallestCircle(points, emptyList())!!.center.run { "${ceil(x).toInt()} ${ceil(y).toInt()}" }
    }
}

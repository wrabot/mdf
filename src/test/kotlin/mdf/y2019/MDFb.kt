package mdf.y2019

import mdf.BaseTest
import org.junit.Test
import tools.board.Board
import tools.board.toBoard
import tools.geometry.Point
import tools.geometry.smallestCircle
import kotlin.math.ceil

class MDFb : BaseTest() {
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
        val board = lines.toBoard { it }
        val king = board.xy.first { board[it] == 'R' }
        val free = board.neighbors8(king).any { !board.isCheck(it) }
        return if (free) "still-in-game" else if (board.isCheck(king)) "check-mat" else "pat"
    }

    private fun Board<Char>.isCheck(xy: Board.XY) = (0..7).any {
        it != xy.y && get(xy.x, it) == 'T'
    } || (0..7).any {
        it != xy.x && get(it, xy.y) == 'T'
    }

    // FIXME outputs 2-8 on website are wrong !!!
    private fun p3(lines: List<String>): Any {
        val points = lines.drop(1).map { it.split(" ").let { (x, y) -> Point(x.toDouble(), y.toDouble()) } }
        return smallestCircle(points, emptyList())!!.center.run { "${ceil(x).toInt()} ${ceil(y).toInt()}" }
    }
}

package mdf.y2019

import mdf.BaseTest
import org.junit.Test
import tools.Point
import tools.XY
import tools.board.Board
import tools.board.toBoard
import tools.geometry.smallestCircle
import tools.read.readAllLines
import kotlin.math.ceil

class MDFb : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(21, ::p2)

    @Test
    fun test3() = test(9, ::p3)

    private fun p1() {
        val result = readAllLines().drop(1).joinToString("").split("X")
            .maxOfOrNull { block -> block.count { it == 'B' } }!! - 1
        println(result)
    }

    private fun p2() {
        val board = readAllLines().toBoard { it }
        val king = board.xy.first { board[it] == 'R' }
        val free = board.neighbors8(king).any { !board.isCheck(it) }
        println(if (free) "still-in-game" else if (board.isCheck(king)) "check-mat" else "pat")
    }

    private fun Board<Char>.isCheck(xy: XY) = (0..7).any {
        it != xy.y && get(xy.x, it) == 'T'
    } || (0..7).any {
        it != xy.x && get(it, xy.y) == 'T'
    }

    // FIXME outputs 2-8 on website are wrong !!!
    private fun p3() {
        val points = readAllLines().drop(1).map { it.split(" ").let { (x, y) -> Point(x.toDouble(), y.toDouble()) } }
        println(smallestCircle(points, emptyList())!!.center.run { "${ceil(x).toInt()} ${ceil(y).toInt()}" })
    }
}

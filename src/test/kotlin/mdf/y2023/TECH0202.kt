package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.Point
import tools.XY
import tools.board.Board
import tools.board.toBoard
import tools.graph.shortPath
import tools.sequence.enumerate
import tools.toPoint
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sign

class TECH0202 : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(3, ::p2)

    @Test
    fun test3() = test(6, ::p3)

    private fun p1(lines: List<String>): Int {
        val clues = lines.drop(1).map { line ->
            line.split(" ").map {
                IndexedValue(
                    it.last().toString().toInt() - 1, when (it.first()) {
                        'R' -> 0
                        'V' -> 1
                        'B' -> 2
                        else -> error("!!!")
                    }
                )
            }.toSet()
        }
        return List(3) { it }.enumerate(5).map {
            val combination = it.withIndex()
            clues.indexOfFirst { clue -> (combination intersect clue).isEmpty() }
        }.map { if (it == -1) clues.size else it }.max()
    }

    private fun p2(lines: List<String>): Int {
        val board = lines.drop(1).toBoard { it }
        return shortPath(
            start = board.xy[board.cells.indexOf('P')],
            end = board.xy[board.cells.indexOf('X')]
        ) { c ->
            Board.xy4dir.mapNotNull { d ->
                var p: XY? = null
                var n = c
                while (true) {
                    n += d
                    when (board[n]) {
                        'o' -> break
                        '#' -> return@mapNotNull p
                    }
                    p = n
                }
                n
            }
        }.size - 1
    }

    private fun p3(lines: List<String>): String {
        val n = lines[0].toInt()
        val top = lines.drop(1).take(n).toPoints()
        val bottom = lines.drop(n + 2).toPoints()
        var t = 1
        var b = 1
        val points = mutableListOf(top[0])
        while (t < top.size || b < bottom.size) {
            val diff = top[t].x - bottom[b].x
            points.add(when (diff.sign) {
                -1.0 -> Point(
                    top[t].x,
                    top[t].y - Shape.interpolate(top[t].x, bottom[b - 1], bottom[b])
                ).also { t++ }

                1.0 -> Point(
                    bottom[b].x,
                    Shape.interpolate(bottom[b].x, top[t - 1], top[t]) - bottom[b].y
                ).also { b++ }

                else -> Point(top[t].x, top[t].y - bottom[b].y).also {
                    t++
                    b++
                }
            })
        }
        val shape = Shape(points)
        return listOf(
            shape.findAbscissaFromArea(shape.areas.last() / 3),
            shape.findAbscissaFromArea(shape.areas.last() * 2 / 3),
        ).joinToString("\n")
    }

    private fun List<String>.toPoints() = map { it.toPoint(" ") }

    private data class Shape(val points: MutableList<Point>) {
        val areas = points.zipWithNext().runningFold(0.0) { acc, (a, b) -> acc + area(a, b.x, b.y) }
        fun findAbscissaFromArea(area: Double): BigDecimal {
            val section = areas.indexOfFirst { it > area } - 1
            val delta = area - areas[section]
            val start = points[section]
            val end = points[section + 1]
            return binarySearch(delta, start.x, end.x, 1e-7) {
                area(start, it, interpolate(it, start, end))
            }.toBigDecimal().setScale(7, RoundingMode.FLOOR)
        }

        fun binarySearch(out: Double, inMin: Double, inMax: Double, inError: Double, f: (Double) -> Double): Double {
            var min = inMin
            var max = inMax
            while (true) {
                val middle = (min + max) / 2
                if (max - min < inError) return middle
                when (f(middle).compareTo(out).sign) {
                    -1 -> min = middle
                    1 -> max = middle
                    else -> return middle
                }
            }
        }

        private fun area(a: Point, x: Double, y: Double) = (a.y + y) / 2 * (x - a.x)

        companion object {
            fun interpolate(x: Double, a: Point, b: Point) = (x - a.x) / (b.x - a.x) * (b.y - a.y) + a.y
        }
    }
}

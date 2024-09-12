package mdf.y2021

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines
import tools.text.toInts

// println(p(lines))
class Orange : BaseTest() {
    @Test
    fun test1() = test(5, ::p1)

    @Test
    fun test2() = test(0, ::p2)

    @Test
    fun test3() = test(4, ::p3)

    @Test
    fun test4() = test(3, ::p4)

    private fun p1() {
        println(readln().zip(readln()).joinToString("") { "${it.first}${it.second}" })
    }

    private fun p2() {
        val result = readln().toInts().plus(0).zipWithNext { a, b ->
            a * 4 - b.coerceAtMost(a)
        }.sum()
        println(result)
    }

    private fun p3() {
        val result = readAllLines().drop(1).mapIndexed { index, line -> Field(index + 1, line) }.run {
            map { field ->
                field to Side.values().sumOf { d ->
                    val border = field.borders[d.opposite]!!
                    count { border.intersect(it.borders[d]!!) }
                }
            }.groupBy({ it.second }, { it.first }).maxBy { it.key }
        }
        println("${result.value.size} ${result.key}\n" + result.value.joinToString(" ") { it.id.toString() })
    }

    private data class Border(val p: Int, val r: IntRange) {
        fun intersect(other: Border) =
            p == other.p && r.first.coerceAtLeast(other.r.first) < r.last.coerceAtMost(other.r.last)
    }

    private data class Field(val id: Int, val borders: Map<Side, Border>) {
        constructor(id: Int, line: String) : this(id, line.split(" ").map { it.toInt() }.let { (x, y, w, h) ->
            mapOf(
                Side.East to Border(x + w, y..y + h),
                Side.West to Border(x, y..y + h),
                Side.North to Border(y + h, x..x + w),
                Side.South to Border(y, x..x + w),
            )
        })
    }

    private enum class Side {
        East, North, West, South;

        val opposite get() = values().let { it[(ordinal + 2).mod(it.size)] }
    }

    private fun p4() {
        val input = readAllLines().map { it.toInts() }
        val target = input[0][1]
        val result = input[1].dropLastWhile { it != target } intersect input[2].dropLastWhile { it != target }.toSet()
        println(result.joinToString(" "))
    }
}

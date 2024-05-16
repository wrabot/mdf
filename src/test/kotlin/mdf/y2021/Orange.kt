package mdf.y2021

import mdf.BaseTest
import org.junit.Test

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

    private fun p1(lines: List<String>) = lines[0].zip(lines[1]).joinToString("") { "${it.first}${it.second}" }

    private fun p2(lines: List<String>) = lines[1].split(" ").map { it.toInt() }.plus(0).zipWithNext { a, b ->
        a * 4 - b.coerceAtMost(a)
    }.sum()

    private fun p3(lines: List<String>) = lines.drop(1).mapIndexed { index, line -> Field(index + 1, line) }.run {
        map { field ->
            field to Side.values().sumOf { d ->
                val border = field.borders[d.opposite]!!
                count { border.intersect(it.borders[d]!!) }
            }
        }.groupBy({ it.second }, { it.first }).maxBy { it.key }
    }.run { "${value.size} $key\n" + value.joinToString(" ") { it.id.toString() } }

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

    private fun p4(lines: List<String>) = lines.map { line -> line.split(" ").map { it.toInt() }}.let {
        val target = it[0][1]
        it[1].dropLastWhile { it != target } intersect it[2].dropLastWhile { it != target }
    }.joinToString(" ")
}

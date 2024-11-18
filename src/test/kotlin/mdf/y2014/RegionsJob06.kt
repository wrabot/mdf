package mdf.y2014

import mdf.BaseTest
import org.junit.Test
import tools.Point
import tools.math.DoubleMatrix
import tools.math.double.toString
import tools.math.gj.gaussJordan
import tools.read.readAllLines
import kotlin.math.sqrt

class RegionsJob06 : BaseTest() {
    @Test
    fun test1() = test(3, ::p1)

    @Test
    fun test2() = test(3, ::p2)

    @Test
    fun test3() = test(3, ::p3)

    @Test
    fun test4() = test(3, ::p4)

    @Test
    fun test5() = test(3, ::p5)

    private fun p1() {
        val cards = readln().split(" ").toSet()
        val values = (2..10).toList() + "VDRA".toList()
        val allCards = "CPQT".flatMap { c -> values.map { "$c$it" } }
        println((allCards - cards).joinToString(" "))
    }

    private fun p2() {
        val data = readAllLines().map {
            val (x, y, hms) = it.split(" ")
            val t = hms.split(":").fold(0) { acc, s -> acc * 60 + s.toInt() }
            Point(x.toDouble(), y.toDouble()) to t
        }
        val d = data.zipWithNext { a, b -> sqrt((b.first - a.first).norm2()) }.sum()
        val t = data.last().second - data.first().second
        println((3.6 * d / t).toString(2))
        DoubleMatrix(4, 5).gaussJordan()
    }

    private fun p3() {
        readAllLines().forEach { line ->
            val (a, b, c, d) = line.split(" ").map { block ->
                block.lowercase().map {
                    when {
                        it.isDigit() -> it
                        it >= 's' -> '2' + (it - 's')
                        else -> '1' + (it - 'a') % 9
                    }
                }.toCharArray().concatToString().toLong()
            }
            val key = 97 - (a * 89 + 15 * b + 3 * c) % 97
            println(if (key == d) "OK" else "KO")
        }
    }

    private fun p4() {
        val map = readAllLines().associate { line ->
            val links = line.split(" ")
            links.first() to links.drop(1)
        }
        val cache = mutableMapOf<String, Int>()
        println(map.keys.maxOf { count(it, map, cache) })
    }

    private fun count(id: String, map: Map<String, List<String>>, cache: MutableMap<String, Int>): Int =
        cache.getOrPut(id) { map[id]?.maxOfOrNull { count(it, map, cache) + 1 } ?: 0 }

    private fun p5() {
        val lines = readAllLines().map { it.split(" ") }
        val plus = lines.filter { it[1] == "+" }.groupBy({ it[0] }, { it[2] })
        val minus = lines.filter { it[1] == "-" }.groupBy({ it[0] }, { it[2] }).mapValues { it.value.toSet() }
        val (origin, _, distance) = lines.last()
        println(Network(plus, minus).neighborhood(origin, distance.toInt()).sorted().joinToString(" "))
    }

    private data class Network(val plus: Map<String, List<String>>, val minus: Map<String, Set<String>>) {
        fun neighborhood(origin: String, distance: Int): Set<String> = if (distance == 0) setOf(origin) else
            plus[origin].orEmpty().flatMap { neighborhood(it, distance - 1) }.toSet() + origin - minus[origin].orEmpty()
    }
}

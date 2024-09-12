package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.graph.distances
import tools.read.readAllLines
import tools.read.readLines

class CAIndividual : BaseTest() {
    @Test
    fun test1() = test(11, ::p1)

    @Test
    fun test2() = test(10, ::p2)

    @Test
    fun test3() = test(17, ::p3)

    private fun p1() {
        println(readAllLines().drop(1).map { it.split(" ") }.minBy { it[1].toInt() }[0])
    }

    private fun p2() {
        val n = readln().toInt()
        val needs = readLines(n).associate {
            val (q, name) = it.split(" ")
            name to q.toInt()
        }
        val stock = readAllLines().drop(1).associate {
            val (q, name) = it.split(" ")
            name to q.toInt()
        }
        println(needs.map { stock[it.key]?.coerceAtMost(it.value) ?: 0 }.sum())
    }

    private fun p3() {
        val n = readln().split(" ")[0].toInt()
        val graph = readAllLines().flatMap { line ->
            val (a, b, c) = line.replace("A", "0").replace("B", (n + 1).toString()).split(" <-> ", " : ")
            listOf(a to (b to c), b to (a to c))
        }.groupBy({ it.first.toInt() }, { it.second }).mapValues { (_, value) ->
            value.groupBy({ it.first.toInt() }, { it.second.toDouble() }).mapValues { it.value.min() }
        }
        val a = distances(n + 2, 0) { graph[it].orEmpty() }
        val b = distances(n + 2, n + 1) { graph[it].orEmpty() }
        println((1..n).joinToString(" ") { (a[it] + b[it]).toInt().toString() })
    }
}

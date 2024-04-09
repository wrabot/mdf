package mdf

import org.junit.Test
import tools.graph.distances

class CAIndividual : BaseTest() {
    @Test
    fun test1() = test(11, ::p1)

    @Test
    fun test2() = test(10, ::p2)

    @Test
    fun test3() = test(1..17, ::p3)

    private fun p1(lines: List<String>) = lines.drop(1).map { it.split(" ") }.minBy { it[1].toInt() }[0]

    private fun p2(lines: List<String>): Int {
        val n = lines[0].toInt()
        val needs = lines.drop(1).take(n).associate {
            val (q, name) = it.split(" ")
            name to q.toInt()
        }
        val stock = lines.drop(n + 2).associate {
            val (q, name) = it.split(" ")
            name to q.toInt()
        }
        return needs.map { stock[it.key]?.coerceAtMost(it.value) ?: 0 }.sum()
    }

    private fun p3(lines: List<String>): String {
        val n = lines[0].split(" ")[0].toInt()
        val graph = lines.drop(1).flatMap { line ->
            val (a, b, c) = line.replace("A", "0").replace("B", (n + 1).toString()).split(" <-> ", " : ")
            listOf(a to (b to c), b to (a to c))
        }.groupBy({ it.first.toInt() }, { it.second }).mapValues { (_, value) ->
            value.groupBy({ it.first.toInt() }, { it.second.toDouble() }).mapValues { it.value.min() }
        }
        val a = distances(n + 2, 0) { graph[it].orEmpty() }
        val b = distances(n + 2, n + 1) { graph[it].orEmpty() }
        return (1..n).joinToString(" ") { (a[it] + b[it]).toInt().toString() }
    }
}

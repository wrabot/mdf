package mdf

import org.junit.Test
import tools.graph.shortPath
import tools.sequence.combinations

class EI2022 : BaseTest() {
    @Test
    fun test1() = test(5, ::p1)

    @Test
    fun test2() = test(5, ::p2)

    @Test
    fun test3() = test(4, ::p3)

    @Test
    fun test4() = test(4, ::p4)

    @Test
    fun test5() = test(3, ::p5)

    private fun p1(lines: List<String>) = lines[0].toSet().size

    private fun p2(lines: List<String>): Int {
        val c = lines[1].split(" ").map { it.toInt() }.sorted()
        return lines[2].split(" ").map { it.toInt() }.sumOf { m -> c.first { m <= it } }
    }

    private fun p3(lines: List<String>): Int {
        val order = lines[1].map { it - 'a' }
        val total = lines[2].toInt()
        return List(9) { it + 1 }.combinations(5).map { digits ->
            val n = digits.toNumber()
            val m = digits.indices.map { digits[order[it]] }.toNumber()
            if (n + m == total) n else 0
        }.first { it != 0 }
    }

    private fun List<Int>.toNumber() = reduce { acc, i -> acc * 10 + i }

    private fun p4(lines: List<String>): Int {
        val graph = lines.drop(1).associate {
            val line = it.split(" ").map(String::toInt)
            line[0] to if (line.size == 2) Node(line[1]) else Node(
                line[1],
                listOf(Link(line[2], line[3]), Link(line[4], line[5]))
            )
        }
        graph.updateTotal(0)
        return graph.values.flatMap { it.children }.maxOf { it.loss * graph[it.child]!!.total }
    }

    private fun Map<Int, Node>.updateTotal(id: Int): Int =
        with(get(id)!!) { (quantity + children.sumOf { updateTotal(it.child) }).also { total = it } }

    private data class Node(val quantity: Int, val children: List<Link> = emptyList(), var total: Int = 0)

    private data class Link(val child: Int, val loss: Int)

    private fun p5(lines: List<String>): Int {
        val (end, max) = lines[0].split(" ").map(String::toInt)
        val graph = lines.drop(1).map {
            val (i, j, d, c) = it.split(" ").map(String::toInt)
            i to (j to Cost(d, c))
        }.groupBy({ it.first }, { it.second }).mapValues { e -> e.value.toMap() }
        return shortPath(
            start = listOf(0),
            isEnd = { it.last() == end },
            cost = { a, b -> graph[a.last()]!![b.last()]!!.carbon.toDouble() }
        ) { current ->
            graph[current.last()]!!.keys.map { current + it }.filter {
                it.zipWithNext { a, b -> graph[a]!![b]!!.duration.toDouble() }.sum() <= max
            }
        }.lastOrNull()?.run { zipWithNext { a, b -> graph[a]!![b]!!.carbon.toDouble() }.sum().toInt() } ?: -1
    }

    private data class Cost(val duration: Int, val carbon: Int)
}



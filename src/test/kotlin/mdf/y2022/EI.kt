package mdf.y2022

import mdf.BaseTest
import org.junit.Test
import tools.graph.shortPath
import tools.read.readAllLines
import tools.sequence.combinations
import tools.text.toInts

class EI : BaseTest() {
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

    private fun p1() = println(readln().toSet().size)

    private fun p2() {
        readln()
        val c = readln().toInts().sorted()
        println(readln().toInts().sumOf { m -> c.first { m <= it } })
    }

    private fun p3() {
        readln()
        val order = readln().map { it - 'a' }
        val total = readln().toInt()
        val result = List(9) { it + 1 }.combinations(5).map { digits ->
            val n = digits.toNumber()
            val m = digits.indices.map { digits[order[it]] }.toNumber()
            if (n + m == total) n else 0
        }.first { it != 0 }
        println(result)
    }

    private fun List<Int>.toNumber() = reduce { acc, i -> acc * 10 + i }

    private fun p4() {
        val graph = readAllLines().drop(1).associate {
            val line = it.split(" ").map(String::toInt)
            line[0] to if (line.size == 2) Node(line[1]) else Node(
                line[1],
                listOf(Link(line[2], line[3]), Link(line[4], line[5]))
            )
        }
        graph.updateTotal(0)
        println(graph.values.flatMap { it.children }.maxOf { it.loss * graph[it.child]!!.total })
    }

    private fun Map<Int, Node>.updateTotal(id: Int): Int =
        with(get(id)!!) { (quantity + children.sumOf { updateTotal(it.child) }).also { total = it } }

    private data class Node(val quantity: Int, val children: List<Link> = emptyList(), var total: Int = 0)

    private data class Link(val child: Int, val loss: Int)

    // TODO refactor zipWithNext
    private fun p5() {
        val (end, max) = readln().toInts()
        val graph = readAllLines().map {
            val (i, j, d, c) = it.toInts()
            i to (j to Cost(d, c))
        }.groupBy({ it.first }, { it.second }).mapValues { e -> e.value.toMap() }
        val result = shortPath(
            start = listOf(0),
            isEnd = { it.last() == end },
            cost = { a, b -> graph[a.last()]!![b.last()]!!.carbon.toDouble() }
        ) { current ->
            graph[current.last()]!!.keys.map { current + it }.filter {
                it.zipWithNext { a, b -> graph[a]!![b]!!.duration.toDouble() }.sum() <= max
            }
        }.lastOrNull()?.run { zipWithNext { a, b -> graph[a]!![b]!!.carbon.toDouble() }.sum().toInt() } ?: -1
        println(result)
    }

    private data class Cost(val duration: Int, val carbon: Int)
}



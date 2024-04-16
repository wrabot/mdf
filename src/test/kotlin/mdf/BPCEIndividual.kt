package mdf

import org.junit.Test
import tools.graph.shortPath

class BPCEIndividual : BaseTest() {
    @Test
    fun test1() = test(9, ::p1)

    @Test
    fun test2() = test(9, ::p2)

    @Test
    fun test3() = test(13, ::p3b)

    private fun p1(lines: List<String>) = lines.drop(1).map {
        val (n, d, v) = it.split(" ")
        n to d.toDouble() / v.toDouble()
    }.maxBy { it.second }.first

    private fun p2(lines: List<String>) = lines.sumOf { line -> line.split('X').sumOf { it.length / 4 } }

    // two solution for p3
    // the first one : find the short path to ensure a solution exist and a recursive search to find an optimal
    // the second one : use a cache for node with depth in reverse way

    private fun p3a(lines: List<String>): Any {
        val (n, _, k) = lines[0].split(" ")
        val graph = lines.drop(1).map { it.split(" ") }.groupBy({ it.first() }, { it.last() })
        val length = k.toInt() + 1
        if (shortPath("1", n) { graph[it].orEmpty() }.size !in 1..length) return "IMPOSSIBLE"
        return graph.findPath(listOf("1"), n, length)?.joinToString(" ") ?: "IMPOSSIBLE"
    }

    private fun Map<String, List<String>>.findPath(path: List<String>, end: String, size: Int): List<String>? {
        if (path.size == size) return if (path.last() == end) path else null
        return get(path.last()).orEmpty().firstNotNullOfOrNull { findPath(path + it, end, size) }
    }

    private fun p3b(lines: List<String>): String {
        val (n, k) = lines[0].split(" ").run { first() to last().toInt() }
        val links = lines.drop(1).map { it.split(" ") }
        var previous = setOf(n)
        val cache = Array(k) {
            previous.flatMap { last -> links.filter { it.last() == last } }.associate { it.first() to it.last() }
                .apply { previous = keys }
        }
        return cache.foldRight(listOf("1")) { map, acc -> acc + (map[acc.last()] ?: return "IMPOSSIBLE") }
            .joinToString(" ")
    }
}

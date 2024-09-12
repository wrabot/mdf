package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.graph.shortPath
import tools.read.readAllLines
import tools.text.toWords

class BPCEIndividual : BaseTest() {
    @Test
    fun test1() = test(9, ::p1)

    @Test
    fun test2() = test(9, ::p2)

    @Test
    fun test3() = test(13, ::p3b)

    private fun p1() {
        val result = readAllLines().drop(1).map {
            val (n, d, v) = it.split(" ")
            n to d.toDouble() / v.toDouble()
        }.maxBy { it.second }.first
        println(result)
    }

    private fun p2() = println(readAllLines().sumOf { line -> line.split('X').sumOf { it.length / 4 } })

    // two solution for p3
    // the first one : find the short path to ensure a solution exist and a recursive search to find an optimal
    // the second one : use a cache for node with depth in reverse way

    private fun p3a() {
        val (n, _, k) = readln().split(" ")
        val graph = readAllLines().map { it.split(" ") }.groupBy({ it.first() }, { it.last() })
        val length = k.toInt() + 1
        if (shortPath("1", n) { graph[it].orEmpty() }.size in 1..length) {
            val path = graph.findPath(listOf("1"), n, length)
            if (path != null) {
                println(path.joinToString(" "))
                return
            }
        }
        println("IMPOSSIBLE")
    }

    private fun Map<String, List<String>>.findPath(path: List<String>, end: String, size: Int): List<String>? {
        if (path.size == size) return if (path.last() == end) path else null
        return get(path.last()).orEmpty().firstNotNullOfOrNull { findPath(path + it, end, size) }
    }

    private fun p3b() = runCatching {
        val (n, k) = readln().toWords().run { first() to last().toInt() }
        val links = readAllLines().map { it.toWords() }
        var previous = setOf(n)
        val cache = Array(k) {
            previous.flatMap { last -> links.filter { it.last() == last } }.associate { it.first() to it.last() }
                .apply { previous = keys }
        }
        val result = cache.foldRight(listOf("1")) { map, acc ->
            acc + (map[acc.last()] ?: error("IMPOSSIBLE"))
        }
        println(result.joinToString(" "))
    }.onFailure { println(it.message) }
}

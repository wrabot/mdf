package mdf.y2024

import mdf.BaseTest
import org.junit.Test
import tools.debug
import tools.graph.mcs
import tools.read.readLines
import tools.text.toInts
import tools.toPoint

class EuroInformation : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(7, ::p2)

    @Test
    fun test3() = test(9, ::p3)

    @Test
    fun test4() = test(6, ::p4)

    @Test
    fun test5() = test(21, ::p5)

    private fun p1() {
        val shoots = readLines(readln().toInt()).map { it.toPoint(" ") }
        println((4.0 * shoots.count { it.norm2() <= 1 } / shoots.size).toString().padEnd(5, '0'))
    }

    private fun p2() {
        val walls = readLines(readln().toInt()).map {
            val (h, c) = it.split(" ")
            h.toInt() to (c == "Black")
        }
        var height = walls.first().first
        var color = walls.first().second
        var group = 1
        var total = 0
        for (wall in walls.drop(1)) {
            if (height >= wall.first) continue
            height = wall.first
            when {
                color == wall.second -> group++
                group > 1 -> {
                    total += group
                    group = 1
                }
            }
            color = wall.second
        }
        if (group > 1) total += group
        println(total)
    }

    private fun p3() {
        val fencers = readLines(readln().toInt()).map { it.toInts() }
        val graph = List(fencers.size) { w ->
            fencers.indices.filter { l ->
                fencers[w].zip(fencers[l]).count { it.first > it.second } >= 2
            }
        }
        var current = listOf(0)
        repeat(4) {
            current = current.flatMap { graph[it] }.distinct()
        }
        println(if (0 in current) "Yes" else "No")
    }

    private fun p4() {
        // +1 => add respiration at start
        val min = readln().toInt() + 1
        val max = readln().toInt() + 1
        val length = readln().toInt() + 1
        println(Solver(min..max).solve(length))
    }

    data class Solver(val range: IntRange) {
        private val cache = mutableMapOf<Int, Long>()
        fun solve(length: Int): Long = when {
            length < 0 -> 0 // already arrived in range
            length < range.first -> 1 // just arrived before range
            else -> cache.getOrPut(length) { range.sumOf { solve(length - it) } }
        }
    }


    private fun p5() {
        val (g, l) = readln().toInts()
        val gates = List(g) { mutableListOf<Int>() }
        readLines(l).forEach {
            val (a, b) = it.toInts()
            gates[a].add(b)
            gates[b].add(a)
        }
        val color = IntArray(g) { -1 }
        mcs(gates.size, 0) { gate ->
            gates[gate].apply {
                val colors = asSequence().map { color[it] }.toSet()
                color[gate] = gates.indices.first { it !in colors }
            }
        }
        println(color.distinct().size)
    }
}

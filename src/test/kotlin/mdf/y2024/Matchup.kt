package mdf.y2024

import mdf.BaseTest
import org.junit.Test
import tools.board.Direction4
import tools.board.toBoard
import tools.graph.shortPath
import tools.optimization.knapsackValue
import tools.read.readAllLines
import tools.read.readLines
import tools.text.toWords
import tools.toPoint
import java.math.BigInteger

class Matchup : BaseTest() {
    @Test
    fun test1() = test(10, ::p1)

    @Test
    fun test2() = test(9, ::p2)

    @Test
    fun test3() = test(10, ::p3)

    @Test
    fun test4() = test(12, ::p4)

    @Test
    fun test5() = test(9, ::p5)

    @Test
    fun test6() = test(1, ::p6)

    private fun p1() {
        println(readAllLines().drop(1).first { it.startsWith("42") && it.sumOf { it - '0' } == 75 })
    }

    private fun p2() {
        println(
            readAllLines().drop(1).map(String::toWords)
                .sortedBy { it[3].toInt() }.sortedBy { it[2].toInt() }.sortedBy { it[1].toInt() }
                .last()[0]
        )
    }

    private fun p3() {
        val board = readAllLines().drop(1).toBoard { it }
        val distances = board.xy.take(board.width).map { c ->
            var d = 0
            var current = c
            val done = mutableSetOf(current)
            while (current.y < board.height) {
                current += Direction4.values().first { it.c == board[current] }.xy
                if (current in done) {
                    d = Int.MAX_VALUE
                    break
                }
                done.add(current)
                d++
            }
            c.x + 1 to d
        }
        val min = distances.minOf { it.second }
        println(distances.filter { it.second == min }.joinToString(" ") { it.first.toString() })
    }

    private fun p4() {
        val width = readln().toInt()
        val start = readln().toPoint(" ")
        val end = readln().toPoint(" ")
        val others = readLines(readln().toInt()).map { it.toPoint(" ") }

        val width2 = width * width
        val all = listOf(start, end) + others
        val map = List(all.size) { point ->
            all.indices.filter { (all[it] - all[point]).norm2() <= width2 }
        }
        val path = shortPath(0, 1) { map[it] }
        if (path.isEmpty()) println(-1) else path.forEach { println(all[it].run { "${x.toInt()} ${y.toInt()}" }) }
    }

    private fun p5() {
        val time = readln().toInt()
        val figures = readLines(readln().toInt()).flatMap { line ->
            val (t, p) = line.split(" ").map { it.toInt() }
            (0..time / t).map { t to (p - it).toDouble() }
        }
        println(knapsackValue(figures, time).toInt())
    }

    private fun p6() {
        val size = readln().toInt()
        val value = readln().toBigInteger(2)
        val nodes = readLines(readln().toInt()).associate { line ->
            val words = line.toWords()
            words[1].toInt() to (words[0] to words.drop(3).map { it.toInt() })
        }

        Circuit(size, nodes).solve(value)!!.toList().sortedBy { it.first }.forEach {
            println(it.second.toString(2).padStart(size, '0'))
        }
    }

    data class Circuit(val size: Int, val nodes: Map<Int, Pair<String, List<Int>>>) {
        private val mask: BigInteger = (BigInteger.ONE shl size) - BigInteger.ONE
        private val outputId = nodes.filter { it.value.first == "OUTPUT" }.keys.single()

        fun solve(value: BigInteger) = solve(outputId, value)

        private fun solve(nodeId: Int, value: BigInteger): Map<Int, BigInteger>? {
            val (op, args) = nodes[nodeId]!!
            return when (op) {
                "INPUT" -> mapOf(nodeId to value)
                "OUTPUT" -> solve(args.single(), value)
                "NOT" -> solve(args.single(), value.not().and(mask))

                "LEFT_SHIFT" ->
                    if (value.testBit(0)) null else solve(args.single(), value.shiftRight(1).and(mask))

                "RIGHT_SHIFT" ->
                    if (value.testBit(size - 1)) null else solve(args.single(), value.shiftLeft(1).and(mask))

                "AND" -> {
                    val left = solve(args.first(), value)
                    val right = solve(args.last(), value)
                    if (left == null || right == null) null else left + right
                }

                "OR", "XOR", "SUM" -> {
                    val left = solve(args.first(), value)
                    if (left != null)
                        left + solve(args.last(), BigInteger.ZERO)!!
                    else
                        solve(args.first(), BigInteger.ZERO)!! + solve(args.last(), value)!!
                }

                else -> error("!!! unexpected op $op")
            }
        }
    }
}

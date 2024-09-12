package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines

class MDFRound1 : BaseTest() {
    @Test
    fun test1() = test(9, ::p1)

    @Test
    fun test2() = test(17, ::p2)

    @Test
    fun test3() = test(16, ::p3)

    private fun p1() {
        val pfc = "PFC"
        println(readln().toList().reduce { a, b ->
            when (pfc.indexOf(a) - pfc.indexOf(b)) {
                1, -2 -> a
                else -> b
            }
        })
    }

    private fun p2() {
        readln()
        val target = readln().toLong() - 1
        val seasons = readln().split(" ").map { it.toLong() }
        var targetInYear = target % seasons.sum()
        seasons.forEachIndexed { index, d ->
            if (targetInYear < d) {
                println(index + 1)
                return
            }
            targetInYear -= d
        }
        println(-1)
    }

    private fun p3() {
        val (n, _, e) = readln().split(" ").map { it.toInt() }
        val lifts = readAllLines().map {
            val (a, b) = it.split(" ").map { it.toInt() }
            a..b
        }
        var current = e
        while (true) {
            val max = lifts.filter { current in it }.maxOfOrNull { it.last } ?: break
            if (max == current) break
            current = max
        }
        println(if (current == n) "YES" else "NO")
    }
}

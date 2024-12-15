package mdf.y2020

import mdf.BaseTest
import org.junit.Test
import tools.optimization.knapsackContent
import tools.text.toInts

class LaPoste : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(4, ::p2)

    @Test
    fun test3() = test(4, ::p3)

    private fun p1() = println(List(readln().toInt()) { readln().split(" ") }.maxBy { it[0].toInt() }[1])
    private fun p2() = println(List(readln().toInt()) { readln().toInts() }.sumOf { (n, p, m) ->
        (n * (p + 125.0 * m / 1000))
    }.toInt() + 495)

    private fun p3() {
        val packets = List(readln().toInt()) { readln().split(" ") }.sortedBy { it[0] }
        val w = packets.map { it[1].toInt() to it[1].toDouble() }
        val content = knapsackContent(w, w.sumOf { it.first } / 2).map { packets[it][0] }.toSet()
        println(content.sorted().joinToString(" "))
        println((packets.map { it[0] } - content).sorted().joinToString(" "))
    }
}

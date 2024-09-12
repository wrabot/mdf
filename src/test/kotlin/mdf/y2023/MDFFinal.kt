package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines

class MDFFinal : BaseTest() {
    @Test
    fun test1() = test(9, ::p1)

    @Test
    fun test2() = test(11, ::p2)

    @Test
    fun test3() = test(0, ::p3)

    private fun p1() {
        readAllLines().drop(1).map { it.split(" ") }
            .groupBy({ it[1] }, { it[0] }).values.mapNotNull { it.singleOrNull()?.toInt() }.sorted()
            .forEach { println(it) }
    }

    private fun p2() {
        val (n, _, _) = readln().split(" ").map { it.toInt() }
        val cheaters = readln().split(" ").map { it.toInt() }.toMutableSet()
        val routes = readAllLines().map { line -> line.split(" ").map { it.toInt() } }
            .flatMap { listOf(it[0] to it[1], it[1] to it[0]) }.groupBy({ it.first }, { it.second })
        val winners = mutableSetOf(1)
        val free = (1..n).minus(winners).minus(cheaters).toMutableSet()
        while (free.isNotEmpty()) {
            (cheaters.flatMap { routes[it]!! } intersect free).run {
                free.removeAll(this)
                cheaters.addAll(this)
            }
            (winners.flatMap { routes[it]!! } intersect free).run {
                free.removeAll(this)
                winners.addAll(this)
            }
        }
        println(winners.sorted().joinToString(" "))
    }

    //TODO
    private fun p3() {}
}

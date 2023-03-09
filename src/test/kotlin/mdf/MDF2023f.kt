package mdf

import org.junit.Test

class MDF2023f : BaseTest("MDF2023f") {
    // println(p(lines))

    @Test
    fun test1() = test(9, ::p1)

    @Test
    fun test2() = test(11, ::p2)

    @Test
    fun test3() = test(0, ::p3)

    @Test
    fun test4() = test(0, ::p4)

    @Test
    fun test5() = test(0, ::p5)

    @Test
    fun test6() = test(0, ::p6)

    private fun p1(lines: List<String>): Any {
        return lines.asSequence().drop(1).map { it.split(" ") }
            .groupBy({ it[1] }, { it[0] }).filter { it.value.size == 1 }
            .map { it.value.single().toInt() }.sorted()
            .joinToString("\n")
    }

    private fun p2(lines: List<String>): Any {
        val (n, _, _) = lines[0].split(" ").map { it.toInt() }
        val cheaters = lines[1].split(" ").map { it.toInt() }.toMutableSet()
        val routes = lines.drop(2).map { line -> line.split(" ").map { it.toInt() } }
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
        return winners.sorted().joinToString(" ")
    }

    private fun p3(lines: List<String>): Any {
        return lines
    }

    private fun p4(lines: List<String>): Any {
        return lines
    }

    private fun p5(lines: List<String>): Any {
        return lines
    }

    private fun p6(lines: List<String>): Any {
        return lines
    }
}

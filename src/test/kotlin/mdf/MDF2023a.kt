package mdf

import org.junit.Test

class MDF2023a : BaseTest() {
    @Test
    fun test1() = test(9, ::p1)

    @Test
    fun test2() = test(17, ::p2)

    @Test
    fun test3() = test(16, ::p3)

    private fun p1(lines: List<String>): Any {
        val pfc = "PFC"
        return lines[0].toList().reduce { a, b ->
            when (pfc.indexOf(a) - pfc.indexOf(b)) {
                1, -2 -> a
                else -> b
            }
        }
    }

    private fun p2(lines: List<String>): Any {
        val target = lines[1].toLong() - 1
        val seasons = lines[2].split(" ").map { it.toLong() }
        var targetInYear = target % seasons.sum()
        seasons.forEachIndexed { index, d ->
            if (targetInYear < d) return index + 1
            targetInYear -= d
        }
        return -1
    }

    private fun p3(lines: List<String>): Any {
        val (n, _, e) = lines[0].split(" ").map { it.toInt() }
        val lifts = lines.drop(1).map {
            val (a, b) = it.split(" ").map { it.toInt() }
            a..b
        }
        var current = e
        while (true) {
            val max = lifts.filter { current in it }.maxOfOrNull { it.last } ?: break
            if (max == current) break
            current = max
        }
        return if (current == n) "YES" else "NO"
    }
}

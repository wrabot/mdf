package mdf

import org.junit.Test

class MDF2023a : BaseTest("MDF2023a") {
    // println(p(lines))

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
        val j = lines[1].toLong() - 1
        val s = lines[2].split(" ").map { it.toLong() }
        val t = s.sum()
        var i = j % t
        s.forEachIndexed { index, d ->
            if (i < d) return index + 1
            i -= d
        }
        return -1
    }

    private fun p3(lines: List<String>): Any {
        val (n, m, e) = lines[0].split(" ").map { it.toInt() }
        val a = lines.drop(1).map {
            val (a, b) = it.split(" ").map { it.toInt() }
            a..b
        }
        val find = find(a.filter { e in it }, a, n)
        return if (find) "YES" else "NO"
    }

    private fun find(e: List<IntRange>, a: List<IntRange>, n: Int): Boolean {
        if (e.any { it.contains(n) }) return true
        val new = a.filter { u ->
            e.any { it.first in u || it.last in u }
        }
        if (new.isEmpty()) return false
        return find(new, a - new, n)
    }
}

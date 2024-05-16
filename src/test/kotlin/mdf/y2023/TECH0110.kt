package mdf.y2023

import mdf.BaseTest
import org.junit.Test

class TECH0110 : BaseTest() {
    @Test
    fun test1() = test(6, ::p1)

    @Test
    fun test2() = test(4, ::p2)

    @Test
    fun test3() = test(3, ::p3)

    private fun p1(lines: List<String>): Any {
        var p = lines[0].toInt()
        repeat(4) {
            p = when {
                p % 3 == 0 -> p / 3
                p % 2 == 0 -> p / 2
                else -> p - 1
            }
        }
        return p
    }

    private fun p2(lines: List<String>): Any {
        val s1 = lines[0].toSet()
        val s2 = lines[1].toSet()
        val m1 = (s1 intersect s2).joinToString("")
        val m2 = (s2 intersect s1).joinToString("")
        return if (m1.isEmpty() || m1 != m2) "NORMAL" else "TEMPETE\n$m1"
    }

    private fun p3(lines: List<String>): Any {
        val links = lines.drop(1).map { it.split(" ") }.groupBy({ it[0] }, { it[1] })
        val groups = mutableMapOf<String, List<String>>()
        return links.keys.maxBy { group(it, links, groups).size }
    }

    private fun group(key: String, links: Map<String, List<String>>, groups: MutableMap<String, List<String>>): List<String> =
        groups.getOrPut(key) { links[key]?.flatMap { group(it, links, groups) } ?: listOf(key) }
}

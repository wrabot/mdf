package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines

class TECH0110 : BaseTest() {
    @Test
    fun test1() = test(6, ::p1)

    @Test
    fun test2() = test(4, ::p2)

    @Test
    fun test3() = test(3, ::p3)

    private fun p1() {
        var p = readln().toInt()
        repeat(4) {
            p = when {
                p % 3 == 0 -> p / 3
                p % 2 == 0 -> p / 2
                else -> p - 1
            }
        }
        println(p)
    }

    private fun p2() {
        val s1 = readln().toSet()
        val s2 = readln().toSet()
        val m1 = (s1 intersect s2).joinToString("")
        val m2 = (s2 intersect s1).joinToString("")
        println(if (m1.isEmpty() || m1 != m2) "NORMAL" else "TEMPETE\n$m1")
    }

    private fun p3() {
        val links = readAllLines().drop(1).map { it.split(" ") }.groupBy({ it[0] }, { it[1] })
        val groups = mutableMapOf<String, List<String>>()
        println(links.keys.maxBy { group(it, links, groups).size })
    }

    private fun group(
        key: String,
        links: Map<String, List<String>>,
        groups: MutableMap<String, List<String>>
    ): List<String> =
        groups.getOrPut(key) { links[key]?.flatMap { group(it, links, groups) } ?: listOf(key) }
}

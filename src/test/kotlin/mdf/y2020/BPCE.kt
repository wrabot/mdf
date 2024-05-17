package mdf.y2020

import mdf.BaseTest
import org.junit.Test
import tools.graph.distances
import tools.range.merge
import tools.range.size

class BPCE : BaseTest() {
    @Test
    fun test1() = test(2, ::p1)

    @Test
    fun test2() = test(3, ::p2)

    @Test
    fun test3() = test(2, ::p3)

    @Test
    fun test4() = test(2, ::p4)

    @Test
    fun test5() = test(2, ::p5)

    private fun p1(lines: List<String>) = lines.asSequence().drop(1).map { it.split(" ") }.groupBy({ it[1] }, { it[0] })
        .mapNotNull { it.value.singleOrNull() }.first()

    private fun p2(lines: List<String>): String {
        val n = lines[0].toInt()
        val text = lines[1]
        val h = text.length / n
        return (0 until n).flatMap { c -> (0 until h).map { text[it * n + c] } }.joinToString("")
    }

    private fun p3(lines: List<String>) = lines.drop(1).map { line ->
        val (hs, ms, he, me) = line.split(':', '-').map { it.toInt() }
        val start = hs * 60L + ms
        var end = he * 60L + me
        if (end < start) end += 24 * 60
        start..end
    }.merge().run { plusElement(first().move(24 * 60)) }.zipWithNext { a, b ->
        a.last + 1 until b.first
    }.maxBy { it.size }.run {
        if (isEmpty()) "IMPOSSIBLE" else "${first.toTime()}-${last.toTime()}"
    }

    private fun LongRange.move(offset: Int) = first + offset..last + offset
    private fun Long.toTime() = "${((this / 60) % 24).pad()}:${(this % 60).pad()}"
    private fun Long.pad() = toString().padStart(2, '0')

    private fun p4(lines: List<String>): String {
        val origin = lines[1].toList()
        val target = lines[2].toList()
        return (0..25).asSequence().flatMap { d ->
            val caesar = origin.map { 'a' + (it + d - 'a') % 26 }
            caesar.indices.asSequence().map { r ->
                caesar.subList(r, caesar.size) + caesar.subList(0, r)
            }
        }.maxBy { it.zip(target) { a, b -> if (a == b) 1 else 0 }.sum() }.joinToString("")
    }

    private fun p5(lines: List<String>): String {
        val (n, _, a) = lines[0].split(" ").map(String::toInt)
        val neighbors = lines.drop(1).flatMap {
            val (u, v) = it.split(" ").map(String::toInt)
            listOf(u to v, v to u)
        }.groupBy({ it.first }, { it.second }).mapValues { it.value.associateWith { 1.0 } }
        return distances(n + 1, a) { neighbors[it].orEmpty() }.drop(1).withIndex()
            .groupBy({ it.value }, { it.index + 1 }).maxBy { it.key }.value.sorted().joinToString(" ")
    }
}

package mdf.y2022

import mdf.BaseTest
import org.junit.Test
import tools.log
import tools.sequence.combinations

class Thales : BaseTest() {
    @Test
    fun test1() = test(5, ::p1)

    @Test
    fun test2() = test(5, ::p2)

    @Test
    fun test3() = test(6, ::p3)

    @Test
    fun test4() = test(0, ::p4)


    private fun p1(lines: List<String>): Int {
        return lines[0].toInt().square() - lines[1].toInt().square()
    }

    private fun Int.square() = this * this

    private fun p2(lines: List<String>): Int {
        val tanks = lines[1].split(" ").map(String::toInt)
        val max = tanks.max()
        return tanks.sumOf { max - it }
    }

    // TODO test on server 
    private fun p3(lines: List<String>) = (0..4).toList().combinations().minBy { c ->
        c.map { i -> lines[i].filter { it.isDigit() } }.reduce { a, b ->
            val i = (4 downTo 0).first { a.substring(a.length - it) == b.substring(0, it) }
            a + b.substring(i)
        }.length
    }.map { 'A' + it }.joinToString("")

    private fun p4(lines: List<String>): Any {
        return lines
    }
}

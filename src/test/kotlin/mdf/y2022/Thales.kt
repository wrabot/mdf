package mdf.y2022

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines
import tools.sequence.combinations
import tools.text.toInts

class Thales : BaseTest() {
    @Test
    fun test1() = test(5, ::p1)

    @Test
    fun test2() = test(5, ::p2)

    @Test
    fun test3() = test(6, ::p3)

    @Test
    fun test4() = test(0, ::p4)


    private fun p1() = println(readln().toInt().square() - readln().toInt().square())

    private fun Int.square() = this * this

    private fun p2() {
        readln()
        val tanks = readln().toInts()
        val max = tanks.max()
        println(tanks.sumOf { max - it })
    }

    // TODO test on server 
    private fun p3() {
        val input = readAllLines().map { line -> line.filter { it.isDigit() } }
        val result = (0..4).toList().combinations().minBy { c ->
            c.map { i -> input[i] }.reduce { a, b ->
                val i = (4 downTo 0).first { a.substring(a.length - it) == b.substring(0, it) }
                a + b.substring(i)
            }.length
        }
        println(result.map { 'A' + it }.joinToString(""))
    }

    private fun p4() {}
}

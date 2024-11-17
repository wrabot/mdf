package mdf.y2022

import mdf.BaseTest
import org.junit.Test
import tools.read.readLines
import tools.text.toInts
import kotlin.math.abs

class CA : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(5, ::p2)

    @Test
    fun test3() = test(6, ::p3)

    @Test
    fun test4() = test(5, ::p4)

    private fun p1() {
        println(readln().let { it.first().toString().repeat(it.length) })
    }

    private fun p2() {
        val heights = readln().toInts()
        var count = 1
        var current = 0
        heights.forEach {
            current += it
            if (current > 10) {
                current = it
                count++
            }
        }
        println(count)
    }

    private fun p3() {
        val deltas = listOf(0) + readln().toInts().zipWithNext { a, b -> b.compareTo(a) }.filter { it != 0 }
        val counts = deltas.zipWithNext().groupingBy { (a, b) -> if (a != b) b else 0 }.eachCount()
        println("${counts[1] ?: 0} ${counts[-1] ?: 0}")
    }

    private fun p4() {
        readLines(readln().toInt()).map { Quadrilateral(it.toInts()) }.forEach {
            println(it.count)
        }
    }

    data class Quadrilateral(val sides: List<Int>) {
        val s = sides[0]
        val w = sides[1]
        val n = sides[2]
        val e = sides[3]

        val count = when {
            2 * sides.max() > sides.sum() -> -1
            s == n && w == e -> 4 // rectangle => sw && se && nw && ne
            abs(s.sq - n.sq) == abs(w.sq - e.sq) -> 2 // se && nw || sw && ne
            (s - n).sq == abs(w.sq - e.sq) -> 2 // se && ne || sw && nw
            (w - e).sq == abs(s.sq - n.sq) -> 2 // sw && se || nw && ne
            s.sq + e.sq in (n - w).sq..(n + w).sq -> 1 // on se
            s.sq + w.sq in (n - e).sq..(n + e).sq -> 1 // on sw
            n.sq + e.sq in (s - w).sq..(s + w).sq -> 1 // on ne
            n.sq + w.sq in (s - e).sq..(s + e).sq -> 1 // on nw
            else -> 0
        }

        private val Int.sq get() = this * this
    }
}

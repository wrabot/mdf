package mdf.y2024

import mdf.BaseTest
import org.junit.Test
import tools.board.Board
import tools.board.toBoard
import tools.read.readAllLines
import tools.read.readLines
import tools.text.toInts
import tools.text.toWords

class CA : BaseTest() {
    @Test
    fun test1() = test(8, ::p1)

    @Test
    fun test2() = test(16, ::p2)

    @Test
    fun test3() = test(10, ::p3)

    private fun p1() {
        val best = readLines(readln().toInt()).map { line ->
            val words = line.toWords()
            words.first() to words.drop(1).map { it.toDouble() }.let { it.sum() / it.count() }
        }.maxBy { it.second }
        println(best.first)
    }

    private fun p2() {
        val eyes = readln().toInt()
        val legs = readln().toInt()
        val tails = readln().toInt()
        val humans = eyes / 2 - tails
        val dogs = legs / 2 - tails - humans
        val birds = tails - dogs
        if (eyes % 2 == 0 && legs % 2 == 0 && humans >= 0 && dogs >= 0 && birds >= 0) {
            println(humans)
            println(dogs)
            println(birds)
        } else {
            println("Hallucination")
        }
    }

    private fun p3() {
        var board = readLines(readln().toInts().last()).toBoard { it.toString().toInt() }
        val cache = mutableMapOf(board.toString() to 0)
        repeat(1000) {
            board = board.next()
            val previous = cache.put(board.toString(), it + 1)
            if (previous != null) {
                println(previous)
                println(it + 1 - previous)
                return
            }
        }
    }

    private fun Board<Int>.next(): Board<Int> {
        val newCells = xy.map { current ->
            val value = get(current)
            if (value > 1) return@map value - 1
            val neighbors = neighbors4(current).map { get(it) }
            when {
                neighbors.count { it == 5 } >= 1 -> 5
                neighbors.count { it == 1 } >= 2 -> 1
                else -> 3
            }
        }
        return Board(width, height, newCells)
    }
}

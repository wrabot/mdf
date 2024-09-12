package mdf.y2019

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines
import tools.text.toInts

class MDFd : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(8, ::p2)

    @Test
    fun test3() = test(2..5, ::p3)

    private fun p1() {
        val (myStart, myLine) = readln().toInts()
        val (hisStart, hisLine) = readln().toInts()
        val myStops = (myStart..36 step myLine).toList()
        val hisStops = (hisStart..36 step hisLine).toList()
        println((myStops intersect hisStops).first())
    }

    private fun p2() {
        val message = readln()
        val text = readAllLines().drop(1).joinToString("")
        var index = 0
        message.forEach {
            val find = text.indexOf(it, index)
            if (find == -1) {
                println(0)
                return
            }
            index = find
        }
        println(1)
    }

    private fun p3() {
        val players = readAllLines().drop(1).mapIndexed { index, line ->
            Player(index + 1, line.split(" ").map { it.toInt() })
        }
        println(players.sort().joinToString(" ") { it.id.toString() })
    }

    data class Player(val id: Int, val times: List<Int>) {
        // FIXME weird order: samples are last to first
        fun isBefore(other: Player) = times.zip(other.times).count { it.first >= it.second } >= 2
    }

    private fun List<Player>.sort(): List<Player> {
        val first = firstOrNull() ?: return emptyList()
        val partition = drop(1).partition { it.isBefore(first) }
        return partition.first.sort() + first + partition.second.sort()
    }
}

package mdf

import org.junit.Test

class MDF2019d : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(8, ::p2)

    @Test
    fun test3() = test(2..5, ::p3)

    private fun p1(lines: List<String>): Any {
        val (myStart, myLine) = lines[0].split(" ").map { it.toInt() }
        val (hisStart, hisLine) = lines[1].split(" ").map { it.toInt() }
        val myStops = (myStart..36 step myLine).toList()
        val hisStops = (hisStart..36 step hisLine).toList()
        return (myStops intersect hisStops).first()
    }

    private fun p2(lines: List<String>): Any {
        val message = lines[0]
        val text = lines.drop(2).joinToString("")
        var index = 0
        message.forEach {
            val find = text.indexOf(it, index)
            if (find == -1) return 0
            index = find
        }
        return 1
    }

    private fun p3(lines: List<String>): Any {
        val players = lines.drop(1).mapIndexed { index, line ->
            Player(index + 1, line.split(" ").map { it.toInt() })
        }
        return players.sort().joinToString(" ") { it.id.toString() }
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

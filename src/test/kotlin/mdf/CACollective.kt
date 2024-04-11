package mdf

import org.junit.Test
import tools.match

class CACollective : BaseTest() {
    @Test
    fun test1() = test(17, ::p1)

    @Test
    fun test2() = test(15, ::p2)

    @Test
    fun test3() = test(1..18, ::p3)

    private fun p1(lines: List<String>): Int {
        val servers = listOf(20 to 1000, 100 to 500, 200 to 300)
        val activity = lines[0].dropLast(1).toInt()
        val inactivity = 100 - activity
        return servers.withIndex().minBy { it.value.run { first * inactivity + second * activity } }.index + 1
    }

    private fun p2(lines: List<String>) = lines.drop(1).flatMap { it.windowed(10).distinct() }
        .groupingBy { it }.eachCount().toList().sortedBy { it.first }.maxBy { it.second }.run { "$first $second" }

    private fun p3(lines: List<String>): String {
        val (w, h) = lines[0].split(" ").map { it.toInt() }
        val (d, i) = lines.drop(2).partition { it[0] == 'D' }.run {
            val dRegex = "D \\((.*),(.*)\\) \\((.*),(.*)\\)".toRegex()
            val iRegex = "I (.*)->(.*) (.*)->(.*)".toRegex()
            first.flatMap { line ->
                val (x1, y1, x2, y2) = line.match(dRegex)!!.map { it.toInt() }
                val chair1 = Chair(x1, y1)
                val chair2 = Chair(x2, y2)
                listOf(chair1 to chair2, chair2 to chair1)
            }.groupBy({ it.first }, { it.second }) to second.map { line ->
                val (x1, x2, y1, y2) = line.match(iRegex)!!.map { it.toInt() }
                x1..x2 to y1..y2
            }
        }
        val room = Array(h) { BooleanArray(w) }
        if (!solve(d, i, room)) return "IMPOSSIBLE"
        require(room.check(d, i) == null)
        return room.toText()
    }

    private data class Chair(val x: Int, val y: Int)

    private fun Array<BooleanArray>.toText() = joinToString("\n") {
        it.joinToString("") { if (it) "B" else "A" }
    }

    private fun Array<BooleanArray>.check(d: Map<Chair, List<Chair>>, i: List<Pair<IntRange, IntRange>>): Any? {
        indices.forEach { y ->
            get(y).indices.forEach { x ->
                val current = get(y)[x]
                i.filter { x in it.first && y in it.second }.forEach { block ->
                    if (block.second.any { yb -> block.first.any { xb -> get(yb)[xb] != current } }) return block
                }
                d[Chair(x, y)].orEmpty().forEach { c ->
                    if (get(c.y)[c.x] == current) Chair(x, y) to c
                }
            }
        }
        return null
    }

    private fun solve(
        d: Map<Chair, List<Chair>>,
        i: List<Pair<IntRange, IntRange>>,
        room: Array<BooleanArray>,
    ): Boolean {
        room.indices.forEach { y ->
            room[y].indices.forEach { x ->
                val todo = mutableListOf(Chair(x, y))
                val done = mutableListOf<Chair>()
                while (true) {
                    val chair = todo.removeLastOrNull() ?: break
                    done.add(chair)
                    val current = room[chair.y][chair.x]
                    i.filter { chair.x in it.first && chair.y in it.second }.forEach { block ->
                        block.first.forEach { xb ->
                            block.second.forEach { yb ->
                                if (room[yb][xb] != current) {
                                    val c = Chair(xb, yb)
                                    if (c in done) return false
                                    todo.add(c)
                                    room[yb][xb] = current
                                }
                            }
                        }
                    }
                    d[chair].orEmpty().forEach { c ->
                        if (room[c.y][c.x] == current) {
                            if (c in done) return false
                            todo.add(c)
                            room[c.y][c.x] = !current
                        }
                    }
                }
            }
        }
        return true
    }
}

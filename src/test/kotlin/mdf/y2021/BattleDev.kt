package mdf.y2021

import mdf.BaseTest
import org.junit.Test
import tools.board.Board
import tools.board.toBoard
import tools.board.toXY
import tools.graph.distances
import tools.optimization.knapsackValue

class BattleDev : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(6, ::p2)

    @Test
    fun test3() = test(3, ::p3)

    @Test
    fun test4() = test(3, ::p4)

    @Test
    fun test5() = test(3, ::p5)

    @Test
    fun test6() = test(10, ::p6)

    private fun p1(lines: List<String>): Int {
        val d = lines[0].toInt()
        val l = lines[1].toInt()
        return d + 5 * l
    }

    private fun p2(lines: List<String>) =
        lines.drop(1).groupingBy { it }.eachCount().filterValues { it == 2 }.keys.first()

    private fun p3(lines: List<String>): String {
        val board = lines.toBoard { it }
        return board.xRange.firstOrNull { x ->
            val bottom = board.yRange.firstOrNull { y -> board[x, y] != '.' } ?: board.height
            if (bottom < 4) return@firstOrNull false
            (bottom - 4 until bottom).all { y -> board.xRange.all { it == x || board[it, y] == '#' } }
        }?.let { "BOOM ${it + 1}" } ?: "NOPE"
    }

    private fun p4(lines: List<String>): Int {
        val trash = lines[1]
        val size = trash.length
        val cache = mutableListOf(mapOf(trash.first() to 1))
        repeat(size - 1) {
            val map = cache.last().toMutableMap()
            val t = trash[it + 1]
            map[t] = map.getOrZero(t) + 1
            cache.add(map)
        }
        val total = cache[trash.lastIndex]
        return (0 until size / 2).count { i ->
            total.all { it.value == (cache[i + size / 2].getOrZero(it.key) - cache[i].getOrZero(it.key)) * 2 }
        } * 2
    }

    private fun Map<Char, Int>.getOrZero(key: Char) = getOrDefault(key, 0)

    private fun p5(lines: List<String>): Int {
        val (_, on, off) = lines[0].split(" ").map(String::toInt)
        val cycle = on + off
        val asteroids = lines[1].split(" ").map(String::toInt)
        var shield = 0
        val cache = IntArray(asteroids.size + cycle) // + cycle to avoid index issue
        asteroids.indices.reversed().forEach {
            shield -= asteroids.getOrNull(it + on) ?: 0
            shield += asteroids[it]
            cache[it] = cache[it + 1].coerceAtLeast(shield + cache[it + cycle])
        }
        return asteroids.sum() - cache[0]
    }

    private fun p6(lines: List<String>): Int {
        // parsing
        var start = 1
        var end = start + lines[0].split(" ").first().toInt()
        val galaxy = lines.subList(start, end).toBoard { it }
        start = end
        end += 1 + lines[start++].split(" ").first().toInt()
        val planetPattern = lines.subList(start, end).toBoard { it }
        val structural = lines[++end].split(" ").map { it.first() }
        start = ++end + 1
        end = start + lines[end].toInt()
        val minerals = lines.subList(start, end).toMap()
        start = end
        end = start + structural.size + minerals.size
        val costs = lines.subList(start, end).toMap()
        val base = lines[end++].toXY(" ").run { Board.XY(y, x) }
        val days = lines[end].toInt()

        // planet matching
        val planetInside = planetPattern.xy.filter { planetPattern[it] == '*' }
        val planetStructural = planetPattern.xy.filter { planetPattern[it] in structural }
        val planetMineral = planetPattern.xy.filter { planetPattern[it] == '-' } + planetInside
        val planetMining = planetStructural.flatMap { xy -> Board.xy4dir.map { xy + it } }.toSet()
        val planetXMax = galaxy.width - planetPattern.width
        val planetYMax = galaxy.height - planetPattern.height
        val planets = galaxy.xy.filter { xy ->
            xy.x <= planetXMax && xy.y <= planetYMax &&
                    planetMineral.all { galaxy[xy + it] !in structural } &&
                    planetStructural.all { galaxy[xy + it] == planetPattern[it] }
        }

        // all distances from base to planet and back
        val baseCost = costs[galaxy[base]]!!
        val neighbors = galaxy.xy.map { xy ->
            galaxy.neighbors4(xy).associate { galaxy.indexOf(it)!! to costs[galaxy[it]]!!.toDouble() }
        }
        val distances = distances(galaxy.xy.size, galaxy.indexOf(base)!!) { neighbors[it] }.mapIndexed { index, d ->
            baseCost + 2 * d.toInt() - costs[galaxy.cells[index]]!!
        }

        // compute days/euros (weight/values for knapsack)
        val mining = planets.map { xy ->
            val cost = planetMining.mapNotNull { galaxy.indexOf(xy + it) }.minOf { distances[it] }
            cost to planetInside.sumOf { minerals[galaxy[xy + it]]!! }.toDouble()
        }

        return knapsackValue(mining, days).toInt()
    }

    private fun List<String>.toMap() = associate {
        val (k, v) = it.split(" ")
        k.first() to v.toInt()
    }
}

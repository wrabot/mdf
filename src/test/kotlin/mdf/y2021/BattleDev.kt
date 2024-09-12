package mdf.y2021

import mdf.BaseTest
import org.junit.Test
import tools.XY
import tools.board.toBoard
import tools.graph.distances
import tools.optimization.knapsackValue
import tools.read.readAllLines
import tools.read.readLines
import tools.text.toInts
import tools.toXY

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

    private fun p1() {
        val d = readln().toInt()
        val l = readln().toInt()
        println(d + 5 * l)
    }

    private fun p2() =
        println(readAllLines().drop(1).groupingBy { it }.eachCount().filterValues { it == 2 }.keys.first())

    private fun p3() {
        val board = readAllLines().toBoard { it }
        val result = board.xRange.firstOrNull { x ->
            val bottom = board.yRange.firstOrNull { y -> board[x, y] != '.' } ?: board.height
            if (bottom < 4) return@firstOrNull false
            (bottom - 4 until bottom).all { y -> board.xRange.all { it == x || board[it, y] == '#' } }
        }?.let { "BOOM ${it + 1}" } ?: "NOPE"
        println(result)
    }

    private fun p4() {
        readln()
        val trash = readln()
        val size = trash.length
        val cache = mutableListOf(mapOf(trash.first() to 1))
        repeat(size - 1) {
            val map = cache.last().toMutableMap()
            val t = trash[it + 1]
            map[t] = map.getOrZero(t) + 1
            cache.add(map)
        }
        val total = cache[trash.lastIndex]
        val result = (0 until size / 2).count { i ->
            total.all { it.value == (cache[i + size / 2].getOrZero(it.key) - cache[i].getOrZero(it.key)) * 2 }
        } * 2
        println(result)
    }

    private fun Map<Char, Int>.getOrZero(key: Char) = getOrDefault(key, 0)

    private fun p5() {
        val (_, on, off) = readln().toInts()
        val cycle = on + off
        val asteroids = readln().toInts()
        var shield = 0
        val cache = IntArray(asteroids.size + cycle) // + cycle to avoid index issue
        asteroids.indices.reversed().forEach {
            shield -= asteroids.getOrNull(it + on) ?: 0
            shield += asteroids[it]
            cache[it] = cache[it + 1].coerceAtLeast(shield + cache[it + cycle])
        }
        println(asteroids.sum() - cache[0])
    }

    private fun p6() {
        // parsing
        val galaxy = readLines(readln().toInts().first()).toBoard { it }
        val planetPattern = readLines(readln().toInts().first()).toBoard { it }
        readln() // ignore structural size
        val structural = readln().filter { it.isLetter() }.toList()
        val minerals = readLines(readln().toInt()).toMap()
        val costs = readLines(structural.size + minerals.size).toMap()
        val base = readln().toInts().let { XY(it[1], it[0]) }
        val days = readln().toInt()

        // planet matching
        val planetInside = planetPattern.xy.filter { planetPattern[it] == '*' }
        val planetStructural = planetPattern.xy.filter { planetPattern[it] in structural }
        val planetMineral = planetPattern.xy.filter { planetPattern[it] == '-' } + planetInside
        val planetMining = planetStructural.flatMap { it.neighbors4() }.toSet()
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

        println(knapsackValue(mining, days).toInt())
    }

    private fun List<String>.toMap() = associate {
        val (k, v) = it.split(" ")
        k.first() to v.toInt()
    }
}

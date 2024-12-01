package mdf.y2019

import mdf.BaseTest
import org.junit.Test
import tools.optimization.taskAssignment
import tools.read.readAllLines
import tools.text.toInts

class MDFc : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(8, ::p2)

    //@Test
    fun test3() = test(listOf(1, 2, 3, 5, 6), ::p3)

    private fun p1() {
        val autonomy = 100 * readln().toInt() / readln().toInt()
        val distances = (listOf(0) + readAllLines().map { it.toInt() }).zipWithNext().map { (a, b) -> b - a }
        println(if (distances.all { it <= autonomy }) "OK" else "KO")
    }

    private fun p2() {
        val relations = readAllLines().map { it.toInts() }
            .flatMap { listOf(it[0] to it[1], it[1] to it[0]) }
            .groupBy({ it.first }, { it.second })
        val mines = relations[1]?.toSet()
        if (mines == null) {
            println(-1)
        } else {
            val result = relations.filterKeys { it in mines }
                .mapValues { it.value.intersect(mines).count() }
                .toList()
                .sortedByDescending { it.first }
                .filter { it.second > 0 }
                .maxByOrNull { it.second }
            if (result == null) {
                println(-1)
            } else {
                println(result.first)
            }
        }
    }

    private fun p3() {
        val participants = readln().split(" ").mapIndexed { index, s -> Participant(index + 1, s == "1") }
        val relations = readAllLines().drop(1).map { line -> line.split(" ").map { it.toInt() } }
            .flatMap { listOf(it[0] to it[1], it[1] to it[0]) }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.toSet() }
        val groups = participants.partition { it.isScientist }
        val costs = groups.first.map { s ->
            groups.second.map { l ->
                relations[s.id].orEmpty().intersect(relations[l.id].orEmpty()).count()
            }
        }
        val result = taskAssignment(groups.first.size, groups.second.size) { r, c ->
            val v = costs[r][c]
            // FIXME -v is not working why ? -v seems better then 1/v
            if (v == 0) 999999999 else 1000000 / v
        }.map { groups.first[it.first].id to groups.second[it.second].id }
            // display score
            .apply { sumOf { relations[it.first].orEmpty().intersect(relations[it.second].orEmpty()).count() } }
            .sortedBy { it.first }.joinToString(",") { "${it.first} ${it.second}" }
        println(result)
    }

    data class Participant(val id: Int, val isScientist: Boolean)
}

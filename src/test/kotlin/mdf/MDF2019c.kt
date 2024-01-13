package mdf

import org.junit.Test
import tools.graph.munkres
import tools.log

class MDF2019c : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(8, ::p2)

    @Test
    fun test3() = test(listOf(1, 2, 3, 5, 6), ::p3)

    private fun p1(lines: List<String>): Any {
        val autonomy = 100 * lines[0].toInt() / lines[1].toInt()
        val distances = (listOf(0) + lines.drop(2).map { it.toInt() }).zipWithNext().map { (a, b) -> b - a }
        return if (distances.all { it <= autonomy }) "OK" else "KO"
    }

    private fun p2(lines: List<String>): Any {
        val relations = lines.drop(1).map { line -> line.split(" ").map { it.toInt() } }
            .flatMap { listOf(it[0] to it[1], it[1] to it[0]) }
            .groupBy({ it.first }, { it.second })
        val mines = relations[1]?.toSet() ?: return -1
        return relations.filterKeys { it in mines }.mapValues { it.value.intersect(mines).count() }.toList()
            .sortedByDescending { it.first }.maxByOrNull { it.second }?.takeIf { it.second > 0 }?.first ?: return -1
    }

    private fun p3(lines: List<String>): Any {
        val participants = lines[1].split(" ").mapIndexed { index, s -> Participant(index + 1, s == "1") }
        val relations = lines.drop(2).map { line -> line.split(" ").map { it.toInt() } }
            .flatMap { listOf(it[0] to it[1], it[1] to it[0]) }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.toSet() }
        val groups = participants.partition { it.isScientist }
        val costs = groups.first.map { s ->
            groups.second.map { l ->
                relations[s.id].orEmpty().intersect(relations[l.id].orEmpty()).count()
            }
        }
        return munkres(groups.first.size, groups.second.size) { r, c ->
            val v = costs[r][c]
            // FIXME -v is not working why ? -v seems better then 1/v
            if (v == 0) 999999999 else 1000000 / v
        }.map { groups.first[it.first].id to groups.second[it.second].id }
            // display score
            .apply { sumOf { relations[it.first].orEmpty().intersect(relations[it.second].orEmpty()).count() }.log() }
            .sortedBy { it.first }.joinToString(",") { "${it.first} ${it.second}" }
    }

    data class Participant(val id: Int, val isScientist: Boolean)
}

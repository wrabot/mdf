package mdf

import org.junit.Test

class MDF2019c : BaseTest("MDF2019c") {
    // println(p(lines))

    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(8, ::p2)

    @Test
    fun test3() = test(2, ::p3)


    private fun p1(lines: List<String>): Any {
        val autonomy = 100 * lines[0].toInt() / lines[1].toInt()
        val distances = (listOf(0) + lines.drop(2).map { it.toInt() })
            .zipWithNext().map { (a, b) -> b - a }
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
        return lines
    }
}

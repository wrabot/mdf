package mdf.y2016

import mdf.BaseTest
import org.junit.Test

class Total : BaseTest() {
    @Test
    fun test1() = test(3, ::p1)

    @Test
    fun test2() = test(3, ::p2)

    @Test
    fun test3() = test(3, ::p3)

    @Test
    fun test4() = test(3, ::p4)

    @Test
    fun test5() = test(3, ::p5)

    @Test
    fun test6() = test(3, ::p6)

    private fun p1(lines: List<String>) =
        listOf("violet", "orange", "jaune", "vert", "rose", "bleu").run { get(lines.sumOf { it.toInt() } % size) }

    private fun p2(lines: List<String>): Int {
        var money = lines[0].toInt()
        val spaces = lines[1].split(" ").map { it.toInt() }
        val moves = lines[2].split(" ").map { it.toInt() }.chunked(2).map { it.sum() }
        var current = 0
        moves.forEach {
            current = (current + it) % spaces.size
            if (current == 19) current = 9
            money -= spaces[current]
            if (money < 0) return current + 1
        }
        return -1
    }

    private fun p3(lines: List<String>) = lines[0].fold(emptyList<Pair<Int, Char>>()) { acc, c ->
        val last = acc.lastOrNull()
        if (last?.second == c) acc.dropLast(1) + (last.first + 1 to c) else acc + (1 to c)
    }.joinToString("") { if (it.first > 2) "${it.first}${it.second}" else it.second.toString().repeat(it.first) }

    private fun p4(lines: List<String>): String {
        val q = lines.size / 4
        val range = q until q * 3
        val data = range.flatMap { r ->
            val row = lines[r + 1].trim().split(" ").map { it.toInt() }
            range.map { row[it] }
        }.sorted()
        val median = (data[data.size / 2 - 1] + data[data.size / 2]) / 2.0
        val mode = data.groupingBy { it }.eachCount().toList().sortedBy { it.first }.maxBy { it.second }.first
        return listOf(data.first(), data.last(), median, mode).joinToString(" ")
    }

    private fun p5(lines: List<String>) =
        lines.flatMap { line -> "\\w+".toRegex().findAll(line.lowercase()).map { it.value }.distinct() }
            .groupingBy { it }.eachCount().filter { it.value < lines.size }
            .toList().sortedBy { it.first }.sortedByDescending { it.second }.take(3)
            .joinToString("\n") { "${it.second} ${it.first}" }

    private fun p6(lines: List<String>) = runCatching { lines[0].parseContent(0).first }.getOrElse { it.message!! }

    private fun String.parseTagContent(start: Int): Pair<String, Int> {
        val (startTag, startContent) = parseTag(start)
        if (startTag.startsWith('/')) return "" to start
        val (content, endContent) = parseContent(startContent)
        val (endTag, end) = parseTag(endContent)
        if (endTag != "/$startTag") throw Error("E $endContent ${endTag.drop(1)} $startTag")
        return "($startTag$content)" to end
    }

    private fun String.parseContent(start: Int): Pair<String, Int> {
        val builder = StringBuilder()
        var current = start
        do {
            val (content, endContent) = parseTagContent(current)
            builder.append(content)
            current = endContent
        } while (current < length && content != "")
        return builder.toString() to current
    }

    private fun String.parseTag(offset: Int): Pair<String, Int> {
        val end = indexOf('>', offset)
        return substring(offset + 1, end) to end + 1
    }
}

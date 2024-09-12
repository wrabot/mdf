package mdf.y2016

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines
import tools.text.toInts

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

    private fun p1() {
        val colors = listOf("violet", "orange", "jaune", "vert", "rose", "bleu")
        println(colors[readAllLines().sumOf { it.toInt() } % colors.size])
    }

    private fun p2() {
        var money = readln().toInt()
        val spaces = readln().toInts()
        val moves = readln().toInts().chunked(2).map { it.sum() }
        var current = 0
        moves.forEach {
            current = (current + it) % spaces.size
            if (current == 19) current = 9
            money -= spaces[current]
            if (money < 0) {
                println(current + 1)
                return
            }
        }
        println(-1)
    }

    private fun p3() {
        val result = readln().fold(emptyList<Pair<Int, Char>>()) { acc, c ->
            val last = acc.lastOrNull()
            if (last?.second == c) acc.dropLast(1) + (last.first + 1 to c) else acc + (1 to c)
        }.joinToString("") { if (it.first > 2) "${it.first}${it.second}" else it.second.toString().repeat(it.first) }
        println(result)
    }

    private fun p4() {
        val size = readln().toInt()
        val board = readAllLines().map { it.toInts() }
        val q = size / 4
        val range = q until q * 3
        val data = range.flatMap { r -> range.map { board[r][it] } }.sorted()
        val median = (data[data.size / 2 - 1] + data[data.size / 2]) / 2.0
        val mode = data.groupingBy { it }.eachCount().toList().sortedBy { it.first }.maxBy { it.second }.first
        println(listOf(data.first(), data.last(), median, mode).joinToString(" "))
    }

    private fun p5() {
        val lines = readAllLines()
        val result = lines.flatMap { line -> "\\w+".toRegex().findAll(line.lowercase()).map { it.value }.distinct() }
            .groupingBy { it }.eachCount().filter { it.value < lines.size }
            .toList().sortedBy { it.first }.sortedByDescending { it.second }.take(3)
        println(result.joinToString("\n") { "${it.second} ${it.first}" })
    }

    private fun p6() {
        println(runCatching { readln().parseContent(0).first }.getOrElse { it.message!! })
    }

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

package mdf

import org.junit.Test
import kotlin.math.max

class CDC2023q : BaseTest("CDC2023q") {
    // println(p(lines))

    @Test
    fun test1() = test(11, ::p1)

    @Test
    fun test2() = test(17, ::p2)

    @Test
    fun test3() = test(11, ::p3)

    @Test
    fun test4() = test(16, ::p4)

    private fun p1(lines: List<String>) = lines[0].zip(lines[1]).map {
        max(it.first.code, it.second.code)
    }.joinToString("") { it.toChar().toString() }

    private fun p2(lines: List<String>) =
        if (lines[1].split(" ").map { it.toDouble() }.zipWithNext { a, b -> a - b }.max() > 2.0) "POLLUTION" else "OK"

    private fun p3(lines: List<String>) =
        lines.drop(1).map { line -> line.split("x").map { it.toInt() }.sorted() }
            .groupingBy { it }.eachCount().count { it.value > 1 }


    private fun p4(lines: List<String>) =
        lines.drop(1).map { line ->
            when {
                line.startsWith("ADD") -> Instruction.Add(line.split(" ")[1].toInt())
                line == "TRANSFER" -> Instruction.Transfer
                line == "SWAP" -> Instruction.Swap
                line.startsWith("JUMP") -> Instruction.Jump(line.split(" ")[1].toInt())
                else -> error("!!!")
            }
        }.run {
            var right = 0
            var left = 0
            var offset = 0
            while (offset < size) {
                when (val i = get(offset++)) {
                    is Instruction.Add -> right += i.value
                    Instruction.Swap -> left = right.also { right = left }
                    Instruction.Transfer -> left += right.also { right = 0 }
                    is Instruction.Jump -> if (!i.done) {
                        i.done = true
                        offset += i.value - 1
                    }
                }
            }
            "$left $right"
        }

    sealed class Instruction {
        data class Add(val value: Int) : Instruction()
        object Transfer : Instruction()
        object Swap : Instruction()
        data class Jump(val value: Int, var done: Boolean = false) : Instruction()
    }
}

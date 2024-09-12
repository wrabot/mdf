package mdf.y2023

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines
import tools.text.toWords
import kotlin.math.max

class CDCQualif : BaseTest() {
    @Test
    fun test1() = test(11, ::p1)

    @Test
    fun test2() = test(17, ::p2)

    @Test
    fun test3() = test(11, ::p3)

    @Test
    fun test4() = test(16, ::p4)

    private fun p1() = println(readln().zip(readln()) { a, b -> max(a.code, b.code).toChar() }.joinToString(""))

    private fun p2() {
        readln()
        val input = readln().toWords().map { it.toDouble() }
        val pollution = input.zipWithNext { a, b -> a - b }.max()
        println(if (pollution > 2.0) "POLLUTION" else "OK")
    }

    private fun p3() {
        val result = readAllLines().drop(1).map { line -> line.split("x").map { it.toInt() }.sorted() }
            .groupingBy { it }.eachCount().count { it.value > 1 }
        println(result)
    }

    private fun p4() {
        val instructions = readAllLines().drop(1).map { line ->
            when {
                line.startsWith("ADD") -> Instruction.Add(line.split(" ")[1].toInt())
                line == "TRANSFER" -> Instruction.Transfer
                line == "SWAP" -> Instruction.Swap
                line.startsWith("JUMP") -> Instruction.Jump(line.split(" ")[1].toInt())
                else -> error("!!!")
            }
        }
        var right = 0
        var left = 0
        var offset = 0
        while (offset < instructions.size) {
            when (val i = instructions[offset++]) {
                is Instruction.Add -> right += i.value
                Instruction.Swap -> left = right.also { right = left }
                Instruction.Transfer -> left += right.also { right = 0 }
                is Instruction.Jump -> if (!i.done) {
                    i.done = true
                    offset += i.value - 1
                }
            }
        }
        println("$left $right")
    }

    sealed class Instruction {
        data class Add(val value: Int) : Instruction()
        object Transfer : Instruction()
        object Swap : Instruction()
        data class Jump(val value: Int, var done: Boolean = false) : Instruction()
    }
}

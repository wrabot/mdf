package mdf.y2020

import mdf.BaseTest
import org.junit.Test

class BPCE : BaseTest() {
    @Test
    fun test1() = test(0, ::p1)

    @Test
    fun test2() = test(0, ::p2)

    @Test
    fun test3() = test(0, ::p3)

    @Test
    fun test4() = test(0, ::p4)

    @Test
    fun test5() = test(0, ::p5)

    @Test
    fun test6() = test(0, ::p6)

    private fun p1(lines: List<String>): Any {
        return lines
    }

    private fun p2(lines: List<String>): Any {
        return lines
    }

    private fun p3(lines: List<String>): Any {
        return lines
    }

    private fun p4(lines: List<String>): Any {
        return lines
    }

    private fun p5(lines: List<String>): Any {
        return lines
    }

    private fun p6(lines: List<String>): Any {
        return lines
    }
}

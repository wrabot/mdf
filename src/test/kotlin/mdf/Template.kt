package mdf

import org.junit.Test

class Template : BaseTest() {
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

    private fun p1() {}
    private fun p2() {}
    private fun p3() {}
    private fun p4() {}
    private fun p5() {}
    private fun p6() {}
}

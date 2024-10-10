package mdf

import org.junit.Test

@Suppress("MemberVisibilityCanBePrivate")
class Playground : BaseTest(false) {
    @Test
    fun test() = test(0, ::main)

    fun main() {}
}

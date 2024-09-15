package mdf

import org.junit.Test

@Suppress("MemberVisibilityCanBePrivate")
class Playground : BaseTest(false) {
    @Test
    fun run() = test(0, ::main)

    fun main() {}
}

package mdf

import org.junit.Assert
import org.junit.Rule
import org.junit.rules.TestName
import tools.log
import java.io.File

open class BaseTest {
    private val dir = javaClass.simpleName

    @Rule
    @JvmField
    val testName = TestName()

    fun test(inputCount: Int, block: (List<String>) -> Any) = test(1..inputCount, block)

    fun test(range: IntRange, block: (List<String>) -> Any) = test(range.toList(), block)

    fun test(inputs: List<Int>, block: (List<String>) -> Any) {
        val root = "./src/test/resources/"
        val path = "$dir/${testName.methodName}"
        log("start test $path")
        inputs.forEach { index ->
            log("start test $path input $index")
            Assert.assertEquals(
                resource("$root$path/output$index.txt"),
                block(resource("$root$path/input$index.txt").lines()).toString()
            )
        }
    }

    private fun resource(name: String) = File(name).readText()
}

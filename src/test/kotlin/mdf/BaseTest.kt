package mdf

import generateFile
import org.junit.Assert
import org.junit.Rule
import org.junit.rules.TestName
import tools.debug
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


open class BaseTest(private val generateMain: Boolean = true, private val compact: Boolean = false) {
    private val dir = javaClass.name.removePrefix("mdf.").replace('.', '/')

    @Rule
    @JvmField
    val testName = TestName()

    fun test(inputCount: Int, block: () -> Unit) = test(1..inputCount, block)

    fun test(range: IntRange, block: () -> Unit) = test(range.toList(), block)

    @OptIn(ExperimentalTime::class)
    fun test(inputs: List<Int>, block: () -> Unit) {
        val root = "./src/test/resources/"
        val path = "$dir/${testName.methodName}"
        debug("start test $path")
        val stdIn = System.`in`
        val stdOut = System.out
        try {
            inputs.forEach { index ->
                val output = ByteArrayOutputStream()
                System.setIn(File("$root$path/input$index.txt").inputStream())
                System.setOut(PrintStream(output, true))
                debug("start test $path input $index")
                val duration = measureTime { block() }
                debug("duration $duration")
                Assert.assertEquals(
                    File("$root$path/output$index.txt").readText(),
                    output.toString().trimEnd('\n')
                )
            }
        } finally {
            System.setIn(stdIn)
            System.setOut(stdOut)
        }
        generateFile(javaClass.packageName, javaClass.simpleName, testName.methodName, generateMain, compact)
    }
}

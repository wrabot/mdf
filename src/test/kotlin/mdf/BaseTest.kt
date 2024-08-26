package mdf

import org.jetbrains.kotlin.cli.common.config.addKotlinSourceRoot
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.findDescendantOfType
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType
import org.junit.Assert
import org.junit.Rule
import org.junit.rules.TestName
import tools.log
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


open class BaseTest {
    private val dir = javaClass.name.removePrefix("mdf.").replace('.', '/')

    @Rule
    @JvmField
    val testName = TestName()

    fun test(inputCount: Int, block: (List<String>) -> Any) = test(1..inputCount, block)

    fun test(range: IntRange, block: (List<String>) -> Any) = test(range.toList(), block)

    @OptIn(ExperimentalTime::class)
    fun test(inputs: List<Int>, block: (List<String>) -> Any) {
        val root = "./src/test/resources/"
        val path = "$dir/${testName.methodName}"
        log("start test $path")
        inputs.forEach { index ->
            log("start test $path input $index")
            val result: String
            val duration = measureTime {
                result = block(resource("$root$path/input$index.txt").lines()).toString()
            }
            log("duration $duration")
            Assert.assertEquals(resource("$root$path/output$index.txt"), result)
        }
        generateFile(javaClass.packageName, javaClass.simpleName, testName.methodName)
    }

    private fun resource(name: String) = File(name).readText()

    // generate

    private fun generateFile(packageName: String, className: String, methodName: String) {
        // input for generation
        val stdImports = mutableListOf<FqName>()
        val start: String
        val components = mutableSetOf<KtElement>()
        val files = mutableSetOf<KtFile>()

        // compute
        val imports = mutableListOf<FqName>()
        start = findTestClass(packageName, className, methodName, imports, components)
        imports.findLibs(stdImports, files)

        // generate
        val generated = listOf(stdImports.joinToString("\n") { "import $it" }) +
                "fun main() = println($start(generateSequence(::readLine).toList()))" +
                components.map { "    ${it.text}".trimIndent() } +
                files.flatMap { file ->
                    file.findChildrenByClass(KtNamedDeclaration::class.java).map { it.text }
                }
        File("build/generated.kt").writeText(generated.joinToString("\n\n", postfix = "\n").trimStart())
    }

    private fun findTestClass(
        packageName: String,
        className: String,
        methodName: String,
        imports: MutableList<FqName>,
        components: MutableSet<KtElement>,
    ): String {
        val testClass = environment.getSourceFiles().filter { it.packageFqName.asString() == packageName }
            .firstNotNullOf { file -> file.findDescendantOfType<KtClass> { it.name == className } }
        val body = testClass.body!!.children.filterIsInstance<KtNamedDeclaration>().map {
            it.name!! to it
        }.groupBy({ it.first }, { it.second })
        val start = body[methodName]!!.first().findDescendantOfType<KtCallableReferenceExpression>()!!
            .callableReference.getReferencedName()
        val done = mutableSetOf<String>()
        val todo = mutableListOf(start)
        while (true) {
            val r = todo.removeLastOrNull() ?: break
            if (r in done) continue
            done.add(r)
            components.addAll(body[r].orEmpty().onEach { todo.addAll(it.names()) })
        }
        imports.addAll(testClass.containingKtFile.imports.filter { it.shortName().asString() in done })
        return start
    }

    private fun MutableList<FqName>.findLibs(stdImports: MutableList<FqName>, files: MutableSet<KtFile>) {
        val toolsName = Name.identifier("tools")
        while (true) {
            val import = removeLastOrNull() ?: break
            if (!import.startsWith(toolsName)) {
                stdImports.add(import)
                continue
            }
            val p = import.parent()
            val n = import.shortName().asString()
            files.addAll(environment.getSourceFiles().filter { it.packageFqName == p }.filter { file ->
                file.findChildrenByClass(KtNamedDeclaration::class.java).any { it.name == n }.also {
                    if (it) addAll(file.imports)
                }
            })
        }
    }

    private fun PsiElement.names(): Set<String> =
        getChildrenOfType<KtNameReferenceExpression>().map { it.text }.toSet() + children.flatMap { it.names() }

    private val KtFile.imports get() = importDirectives.mapNotNull { it.importPath?.fqName }

    private val environment by lazy {
        val tools = File(".gradle/vcs-1").listFiles()!!.filter { it.isDirectory }.maxBy { it.lastModified() }
        KotlinCoreEnvironment.createForProduction(
            Disposer.newDisposable(),
            CompilerConfiguration().apply {
                addKotlinSourceRoot("src/test/kotlin")
                addKotlinSourceRoot("$tools/competitive-tools/src/main/kotlin")
            },
            EnvironmentConfigFiles.JVM_CONFIG_FILES
        )
    }
}

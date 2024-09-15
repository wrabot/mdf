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
import java.io.File

fun generateFile(packageName: String, className: String, methodName: String, generateMain: Boolean) {
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
    val generated = listOfNotNull(
        stdImports.joinToString("\n") { "import $it" },
        if (generateMain) "fun main() = $start()" else null,
        *components.map { "    ${it.text}".trimIndent() }.toTypedArray(),
        *files.flatMap { file -> file.findChildrenByClass(KtNamedDeclaration::class.java).map { it.text } }
            .toTypedArray()
    )
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
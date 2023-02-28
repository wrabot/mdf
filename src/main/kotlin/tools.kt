fun String.toInts() = split(" ").map { it.toInt() }

// combinations

fun <T> List<T>.selectCombinations(): List<List<T>> {
    if (isEmpty()) return listOf(emptyList())
    val first = take(1)
    val others = drop(1).selectCombinations()
    return others + others.map { first + it }
}

fun <T> List<T>.orderCombinations(): List<List<T>> {
    if (isEmpty()) return listOf(emptyList())
    return flatMap { first -> minus(first).orderCombinations().map { listOf(first) + it } }
}

fun <T> List<T>.runOrderCombinations(path: List<T>, block: (List<T>) -> Boolean): Boolean {
    if (isEmpty()) return block(path)
    return any { first -> block(path) && minus(first).runOrderCombinations(path + first, block) }
}

// graphs

fun <T> Map<T, List<T>>.runDFS(path: List<T>, block: (List<T>) -> Boolean): Boolean =
    block(path) && get(path.last()).orEmpty().all { it in path || runDFS(path + it, block) }

fun <T> Map<T, List<T>>.runBFS(start: T, block: (List<T>) -> Boolean) = runBFS(emptyList(), emptyList(), emptySet(), block)

private fun <T> Map<T, List<T>>.runBFS(path: List<T>, todo: List<T>, done: Set<T>, block: (List<T>) -> Boolean): Boolean =
    block(path) && todo.plus(get(path.last()).orEmpty().filter { it !in todo && it !in done }).run { first() to drop(1) }
        .let { (current, remains) -> runBFS(path + current, remains, done.plus(current), block) }

fun <T> Map<T, List<T>>.hasPath(path: List<T>) = hasPath(path.first(), path.drop(1))

private fun <T> Map<T, List<T>>.hasPath(first: T, path: List<T>): Boolean {
    val links = get(first) ?: return false
    val next = path.firstOrNull() ?: return true
    return next in links && hasPath(next, path.drop(1))
}

fun <T> Map<T, List<T>>.findCycles(path: List<T>): List<List<T>> {
    val nexts = get(path.last()) ?: emptyList()
    return nexts.flatMap { if (it in path) listOf(path) else findCycles(path + it) }
}

// pairs

fun List<Pair<Int, Int>>.exclude(other: List<Pair<Int, Int>>) = other.fold(this) { acc, v -> acc.flatMap { it.exclude(v) } }

fun Pair<Int, Int>.exclude(other: Pair<Int, Int>) = when {
    other.second < first || second < other.first -> listOf(this)
    other.first <= first && second <= other.second -> emptyList()
    other.first <= first -> listOf(Pair(other.second + 1, second))
    second <= other.second -> listOf(Pair(first, other.first - 1))
    else -> listOf(Pair(first, other.first - 1), Pair(other.second + 1, second))
}

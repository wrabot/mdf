fun <T> List<T>.combinations(length: Int = size): List<List<T>> = if (length <= 0 && isEmpty()) listOf(emptyList()) else
    flatMap { first -> minus(first).combinations(length - 1).map { listOf(first) + it } }

fun List<IntRange>.merge() = mutableListOf<IntRange>().also { merge ->
    sortedBy { it.first }.forEach {
        val last = merge.lastOrNull()
        when {
            last == null -> merge.add(it)
            it.first in last -> merge[merge.lastIndex] = last.first..Integer.max(last.last, it.last)
            else -> merge.add(it)
        }
    }
}

fun IntRange.size() = last - first + 1

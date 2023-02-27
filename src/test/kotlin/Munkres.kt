// https://users.cs.duke.edu/~brd/Teaching/Bio/asmb/current/Handouts/munkres.html
fun munkres(costs: Matrix<Int>): List<Pair<Int, Int>> {
    val transpose = costs.height > costs.width
    val cost = if (transpose) IntMatrix(costs.width, costs.height).apply {
        costs.onEach { r, c, v -> this[c, r] = v }
    } else IntMatrix(costs.height, costs.width).apply {
        costs.onEach { r, c, v -> this[r, c] = v }
    }
    val mask = IntMatrix(cost.height, cost.width)
    val rowCover = BooleanArray(cost.height)
    val colCover = BooleanArray(cost.width)
    val path = mutableListOf<Pair<Int, Int>>()
    var step = Step.One
    while (true) {
        step = when (step) {
            Step.One -> {
                rowCover.indices.onEach { r ->
                    val min = colCover.indices.minOf { cost[r, it] }
                    colCover.indices.onEach { cost[r, it] -= min }
                }
                Step.Two
            }
            Step.Two -> {
                cost.onEach { r, c, v ->
                    if (v == 0 && !rowCover[r] && !colCover[c]) {
                        mask[r, c] = 1 // starred
                        rowCover[r] = true
                        colCover[c] = true
                    }
                }
                rowCover.fill(false)
                colCover.fill(false)
                Step.Three
            }
            Step.Three -> {
                mask.onEach { _, c, v -> if (v == 1) colCover[c] = true }
                if (colCover.count { it } >= rowCover.size) break else Step.Four
            }
            Step.Four -> {
                while (true) {
                    path.clear()
                    val rc = cost.find { r, c -> cost[r, c] == 0 && !rowCover[r] && !colCover[c] } ?: break
                    mask[rc.first, rc.second] = 2 // primed
                    path.add(rc)
                    val column = mask.findInRow(rc.first, 1) ?: break
                    rowCover[rc.first] = true
                    colCover[column] = false
                }
                if (path.isEmpty()) Step.Six else Step.Five
            }
            Step.Five -> {
                while (true) {
                    val column = path.last().second
                    val row = mask.findInColumn(column, 1) ?: break
                    path.add(row to column)
                    path.add(row to mask.findInRow(row, 2)!!)
                }
                path.forEach { mask[it.first, it.second] = if (mask[it.first, it.second] == 1) 0 else 1 }
                rowCover.fill(false)
                colCover.fill(false)
                mask.onEach { r, c, v -> if (v == 2) mask[r, c] = 0 }
                Step.Three
            }
            Step.Six -> {
                val smallest = cost.minOf { r, c -> !rowCover[r] && !colCover[c] }
                cost.onEach { r, c, _ ->
                    if (rowCover[r]) cost[r, c] += smallest
                    if (!colCover[c]) cost[r, c] -= smallest
                }
                Step.Four
            }
        }
    }
    path.clear()
    mask.onEach { r, c, v -> if (v == 1) path.add(if (transpose) c to r else r to c) }
    return path
}

private enum class Step { One, Two, Three, Four, Five, Six }

private fun Matrix<Int>.minOf(filter: (Int, Int) -> Boolean): Int {
    var min = Int.MAX_VALUE
    onEach { r, c, v -> if (filter(r, c) && min > v) min = v }
    return min
}

private fun Matrix<Int>.find(filter: (Int, Int) -> Boolean): Pair<Int, Int>? {
    var find: Pair<Int, Int>? = null
    onEach { r, c, _ -> if (filter(r, c)) find = r to c }
    return find
}

private fun Matrix<Int>.findInColumn(column: Int, value: Int): Int? {
    repeat(height) { if (this[it, column] == value) return it }
    return null
}

private fun Matrix<Int>.findInRow(row: Int, value: Int): Int? {
    repeat(width) { if (this[row, it] == value) return it }
    return null
}

// https://users.cs.duke.edu/~brd/Teaching/Bio/asmb/current/Handouts/munkres.html
fun munkres(height: Int, width: Int, costs: (r: Int, c: Int) -> Int): List<Pair<Int, Int>> {
    val transpose = height > width
    val cost = if (transpose)
        Array(width) { r -> IntArray(height) { c -> costs(c, r) } }
    else
        Array(height) { r -> IntArray(width) { c -> costs(r, c) } }
    val h = if (transpose) width else height
    val w = if (transpose) height else width
    val mask = Array(h) { IntArray(w) }
    val rowCover = BooleanArray(h)
    val colCover = BooleanArray(w)
    val path = mutableListOf<Pair<Int, Int>>()
    var step = Step.One
    while (true) {
        step = when (step) {
            Step.One -> {
                rowCover.indices.onEach { r ->
                    val min = colCover.indices.minOf { cost[r][it] }
                    colCover.indices.forEach { cost[r][it] -= min }
                }
                Step.Two
            }

            Step.Two -> {
                cost.onEach { r, c, v ->
                    if (v == 0 && !rowCover[r] && !colCover[c]) {
                        mask[r][c] = 1 // starred
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
                    var find: Pair<Int, Int>? = null
                    cost.onEach { r, c, _ -> if (cost[r][c] == 0 && !rowCover[r] && !colCover[c]) find = r to c }
                    val rc = find ?: break
                    mask[rc.first][rc.second] = 2 // primed
                    path.add(rc)
                    val column = mask[rc.first].indexOf(1).takeIf { it != -1 } ?: break
                    rowCover[rc.first] = true
                    colCover[column] = false
                }
                if (path.isEmpty()) Step.Six else Step.Five
            }

            Step.Five -> {
                while (true) {
                    val column = path.last().second
                    val row = mask.indexOfFirst { it[column] == 1 }.takeIf { it != -1 } ?: break
                    path.add(row to column)
                    path.add(row to mask[row].indexOf(2))
                }
                path.forEach { mask[it.first][it.second] = if (mask[it.first][it.second] == 1) 0 else 1 }
                rowCover.fill(false)
                colCover.fill(false)
                mask.onEach { r, c, v -> if (v == 2) mask[r][c] = 0 }
                Step.Three
            }

            Step.Six -> {
                var smallest = Int.MAX_VALUE
                cost.onEach { r, c, v -> if (!rowCover[r] && !colCover[c] && smallest > v) smallest = v }
                cost.onEach { r, c, _ ->
                    if (rowCover[r]) cost[r][c] += smallest
                    if (!colCover[c]) cost[r][c] -= smallest
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

private fun Array<IntArray>.onEach(block: (r: Int, c: Int, v: Int) -> Unit) {
    forEachIndexed { r, row ->
        row.forEachIndexed { c, v ->
            block(r, c, v)
        }
    }
}


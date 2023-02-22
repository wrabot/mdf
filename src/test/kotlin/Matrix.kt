data class Matrix(val width: Int, val height: Int = width) {
    fun init(vararg values: Double) = apply {
        assert(values.size == width * height) { "Invalid init ${values.size} == ${width * height}" }
        values.forEachIndexed { index, value -> cells[index] = value }
    }

    fun init(vararg values: Float) = init(*values.map { it.toDouble() }.toDoubleArray())

    operator fun get(column: Int, row: Int): Double {
        check(column, row)
        return cells[row * width + column]
    }

    operator fun set(column: Int, row: Int, value: Double) = apply {
        check(column, row)
        cells[row * width + column] = value
    }

    fun isValid(column: Int, row: Int) = column in 0 until width && row in 0 until height

    private val cells = DoubleArray(width * height)
    private fun check(column: Int, row: Int) = assert(isValid(column, row)) { "invalid column or row $column < $width $row < $height" }
}

fun Matrix.determinant(): Double {
    assert(width == height) { "Not square matrix $width == $height" }
    assert(width >= 2) { "Only matrix >= 2" }
    return determinant(this::get, width, 1)
}

private fun determinant(get: (column: Int, row: Int) -> Double, size: Int, sign: Int): Double =
    if (size == 2) {
        sign * (get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0))
    } else {
        (0 until size).sumOf {
            val s = if (it % 2 == 0) sign else -sign
            s * get(it, 0) * determinant({ c, r -> get((it + 1 + c) % size, 1 + r) }, size - 1, s)
        }
    }

fun test() {
    assert(
        Matrix(2).init(
            1f, 4f,
            2f, 9f,
        ).determinant() == 1.0
    )
    assert(
        Matrix(3).init(
            4f, 5f, 1f,
            9f, 8f, 2f,
            7f, 6f, 3f,
        ).determinant() == -19.0
    )
    assert(
        Matrix(4).init(
            2f, 4f, 5f, 6f,
            -1f, 5f, 6f, 9f,
            3f, 7f, 1f, -6f,
            4f, -2f, 3f, 5f,
        ).determinant() == 108.0
    )
}

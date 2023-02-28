class DoubleMatrix(height: Int, width: Int = height) : Matrix<Double>(height, width, Array(height * width) { 0.0 }) {
    fun init(vararg values: Float) = apply { init(*values.map { it.toDouble() }.toTypedArray()) }
}

fun DoubleMatrix.determinant(): Double {
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

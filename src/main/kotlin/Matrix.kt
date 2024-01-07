abstract class Matrix<T : Any>(val height: Int, val width: Int, private val cells: Array<T>) {
    fun init(vararg values: T) = apply {
        assert(values.size == width * height) { "Invalid init: ${values.size} == ${height * width}" }
        values.forEachIndexed { index, value -> cells[index] = value }
    }

    operator fun get(row: Int, column: Int): T {
        check(row, column)
        return cells[row * width + column]
    }

    operator fun set(row: Int, column: Int, value: T) = apply {
        check(row, column)
        cells[row * width + column] = value
    }

    fun onEach(block: (r: Int, c: Int, v: T) -> Unit) = apply {
        repeat(height) { r ->
            repeat(width) { c ->
                block(r, c, this[r, c])
            }
        }
    }

    fun isValid(row: Int, column: Int) = column in 0 until width && row in 0 until height

    private fun check(row: Int, column: Int) = assert(isValid(row, column)) { "invalid row or column: $row < $height && $column < $width" }

    override fun toString(): String {
        val strings = cells.map { it.toString() }
        val padding = strings.maxOf { it.length }
        return strings.chunked(width).joinToString("\n") { row ->
            row.joinToString(" ") { it.padStart(padding) }
        }
    }
}

class IntMatrix(height: Int, width: Int = height) : Matrix<Int>(height, width, Array(height * width) { 0 })
